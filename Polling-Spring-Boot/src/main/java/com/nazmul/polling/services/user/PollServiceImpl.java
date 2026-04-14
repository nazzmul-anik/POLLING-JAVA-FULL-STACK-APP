package com.nazmul.polling.services.user;

import com.nazmul.polling.dto.OptionsDTO;
import com.nazmul.polling.dto.PollDTO;
import com.nazmul.polling.entity.Options;
import com.nazmul.polling.entity.Poll;
import com.nazmul.polling.entity.User;
import com.nazmul.polling.exceptions.UserNotFoundException;
import com.nazmul.polling.repository.OptionsRepository;
import com.nazmul.polling.repository.PollRepository;
import com.nazmul.polling.repository.VoteRepository;
import com.nazmul.polling.services.email.EmailService;
import com.nazmul.polling.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {

    private final JwtUtil jwtUtil;
    private final PollRepository pollRepository;
    private final OptionsRepository optionsRepository;
    private final VoteRepository voteRepository;
    private final JavaMailSender javaMailSender;
    private final EmailService emailService;

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
        poll.setExpiryDate(new Date(now.getTime() + (5 * 60 * 1000)));
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
                voteRepository. existsByPoll_IdAndUser_IdAndOption_Id(pollId, userId, options.getId()));
        return optionsDTO;
    }
}
