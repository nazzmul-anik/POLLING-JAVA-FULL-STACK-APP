package com.nazmul.polling.services.user;

import com.nazmul.polling.dto.*;
import com.nazmul.polling.entity.*;
import com.nazmul.polling.exceptions.OptionNotFoundException;
import com.nazmul.polling.exceptions.PollNotFoundException;
import com.nazmul.polling.exceptions.UserNotFoundException;
import com.nazmul.polling.repository.*;
import com.nazmul.polling.services.email.EmailService;
import com.nazmul.polling.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {

    private final JwtUtil jwtUtil;
    private final PollRepository pollRepository;
    private final OptionsRepository optionsRepository;
    private final VoteRepository voteRepository;
    private final EmailService emailService;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;


    @Override
    public PollDTO postPoll(PollDTO pollDTO){
        User user = getCurrentUser();
        Poll poll = createPoll(pollDTO, user);
        List<Options> options = createOptions(pollDTO.getOptions(), poll);
        poll.setOptions(options);
        pollRepository.save(poll);
        emailService.sendPollCreatedEmail(user, poll);
        return getPollDTOInService(poll);
    }

    @Override
    public void deletePollById(Long id) {
        pollRepository.deleteById(id);
    }

    @Override
    public List<PollDTO> getAllPolls() {
        return pollRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Poll::getPostedDate).reversed())
                .map(this::getPollDTOInService)
                .toList();
    }

    @Override
    public List<PollDTO> getMyPolls() {
        User user = getCurrentUser();
        return pollRepository.findAllByUserId(user.getId())
                .stream()
                .sorted(Comparator.comparing(Poll::getPostedDate).reversed())
                .map(this::getPollDTOInService)
                .toList();
    }

    private User getCurrentUser(){
        return Optional.ofNullable(jwtUtil.getLoggedInUser())
                .orElseThrow(()-> new UserNotFoundException("This user is not logged in"));
    }

    private Poll createPoll(PollDTO pollDTO, User user){
        Poll poll = new Poll();
        poll.setQuestion(pollDTO.getQuestion());
        Date now = new Date();
        poll.setPostedDate(now);
        poll.setExpiryDate(pollDTO.getExpiryDate());
        poll.setUser(user);
        poll.setTotalVoteCount(0);
        return poll;
    }

    private List<Options> createOptions(List<String> optionTitles, Poll poll){
        return optionTitles.stream()
                .map(title->{
                    Options option = new Options();
                    option.setTitle(title);
                    option.setPoll(poll);
                    option.setVoteCount(0);
                    return option;
                }).toList();
    }

    public PollDTO getPollDTOInService(Poll poll){
        User loggedInUser = jwtUtil.getLoggedInUser();
        PollDTO pollDTO = new PollDTO();
        pollDTO.setId(poll.getId());
        pollDTO.setQuestion(poll.getQuestion());
        pollDTO.setPostedDate(poll.getPostedDate());
        pollDTO.setExpiryDate(poll.getExpiryDate());
        pollDTO.setIsExpired(poll.getExpiryDate() != null && poll.getExpiryDate().before(new Date()));
        pollDTO.setOptionsDTOS(
                poll.getOptions().stream()
                        .map(options->this.getOptionDTO(options, loggedInUser.getId(), poll.getId()))
                        .toList());
        pollDTO.setTotalVoteCount(poll.getTotalVoteCount());

        User pollOwner = poll.getUser(); // User Who posted this poll
        if(loggedInUser != null && pollOwner.getId().equals(loggedInUser.getId())){
            pollDTO.setUsername("YOU");
        }else{
            pollDTO.setUsername(pollOwner.getFirstName() + " " + pollOwner.getLastName());
        }
        pollDTO.setUserId(pollOwner.getId());
        if(loggedInUser != null){
            pollDTO.setVoted(voteRepository.existsByPoll_IdAndUser_Id(poll.getId(), loggedInUser.getId()));
        }
        return pollDTO;
    }

    public OptionsDTO getOptionDTO(Options options, Long userId, Long pollId){
        OptionsDTO optionsDTO = new OptionsDTO();
        optionsDTO.setId(options.getId());
        optionsDTO.setTitle(options.getTitle());
        optionsDTO.setPollId(options.getPoll().getId());
        optionsDTO.setUserVotedThisOption(
                voteRepository.existsByPoll_IdAndUser_IdAndOption_Id(pollId, userId, options.getId()));
        return optionsDTO;
    }

    @Override
    public LikeDTO giveLikeToPoll(Long pollId) {
        Poll poll = getPoll(pollId);
        User user = getCurrentUser();
        Like like = new Like();
        like.setUser(user);
        like.setPoll(poll);
        likeRepository.save(like);
        return like.getLikeDTO();
    }

    @Override
    public CommentDTO postCommentToPoll(CommentDTO commentDTO) {
        User user = getCurrentUser();
        Poll poll = getPoll(commentDTO.getPollId());
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setCommentDate(new Date());
        comment.setUser(user);
        comment.setPoll(poll);
        return commentRepository.save(comment).getCommentDTO();
    }

    @Override
    public VoteDTO postVoteOnPoll(VoteDTO voteDTO) {
        User user = getCurrentUser();

        Poll poll = getPoll(voteDTO.getPollId());
        pollExpiryCheck(poll);
        poll.setTotalVoteCount(poll.getTotalVoteCount() + 1);
        Options options = getOption(voteDTO.getOptionId());
        options.setVoteCount(options.getVoteCount() + 1);
        Vote vote = new Vote();
        vote.setUser(user);
        vote.setPoll(poll);
        vote.setOption(options);
        vote.setPostedDate(new Date());
        return voteRepository.save(vote).getVoteDTO();
    }

    @Override
    public PollDetailsDTO getPollById(Long pollId) {
        Poll poll = getPoll(pollId);
        User user = getCurrentUser();
        List<Like> likesList = likeRepository.findAllByPollId(pollId);
        List<Comment> commentList = commentRepository.findAllByPollId(pollId);
        PollDetailsDTO pollDetailsDTO = new PollDetailsDTO();
        pollDetailsDTO.setPollDTO(getPollDTOInService(poll));
        pollDetailsDTO.getPollDTO().setIsLiked(
                likeRepository.findByPollIdAndUserId(pollId, user.getId()).isPresent());
        pollDetailsDTO.setCommentDTOList(commentList.stream().map(
                comment->{
                    CommentDTO commentDTO = comment.getCommentDTO();
                    if(comment.getUser().getId().equals(user.getId())){
                        commentDTO.setUsername("YOU");
                    }
                    return commentDTO;
                }
        ).toList());
        pollDetailsDTO.setLikeCount((long)likesList.size());
        pollDetailsDTO.setCommentCount((long)commentList.size());
        return pollDetailsDTO;
    }

    public void pollExpiryCheck(Poll poll){
        if(poll.getExpiryDate().before(new Date())){
            throw new PollNotFoundException(poll.getId()+" no. poll already expired!");
        }
    }

    public Poll getPoll(Long pollId){
        return pollRepository.findById(pollId)
                .orElseThrow(()-> new PollNotFoundException(pollId+" no. this poll id is not found."));
    }

    public Options getOption(Long optionId){
        return optionsRepository.findById(optionId)
                .orElseThrow(()-> new OptionNotFoundException(optionId+", this option id is not found."));
    }
}
