package com.nazmul.polling.controller.user;

import com.nazmul.polling.dto.*;
import com.nazmul.polling.entity.Poll;
import com.nazmul.polling.services.user.PollService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PollController {

    private final PollService pollService;

    @PostMapping("/poll")
    public ResponseEntity<?> postPoll(@RequestBody PollDTO pollDTO){
        PollDTO creatdPollDTO = pollService.postPoll(pollDTO);
        return new ResponseEntity<>(creatdPollDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/poll/{id}")
    public ResponseEntity<?> deletePoll(@PathVariable Long id){
        pollService.deletePollById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/my-polls")
    public ResponseEntity<?> getMyPolls(){
        List<PollDTO> myPolls = pollService.getMyPolls();
        return new ResponseEntity<>(myPolls, HttpStatus.OK);
    }

    @GetMapping("/polls")
    public ResponseEntity<?> getAllPolls(){
        List<PollDTO> allPolls = pollService.getAllPolls();
        return new ResponseEntity<>(allPolls, HttpStatus.OK);
    }

    @GetMapping("/poll/like/{id}")
    public ResponseEntity<?> giveLikeToPoll(@PathVariable("id") Long pollId){
        LikeDTO likeDTO = pollService.giveLikeToPoll(pollId);
        return new ResponseEntity<>(likeDTO, HttpStatus.CREATED);
    }

    @PostMapping("/poll/comment")
    public ResponseEntity<?> postCommentOnPoll(@RequestBody CommentDTO commentDTO){
        CommentDTO postedCommentDTO = pollService.postCommentToPoll(commentDTO);
        return new ResponseEntity<>(postedCommentDTO, HttpStatus.CREATED);
    }

    @PostMapping("/poll/vote")
    public ResponseEntity<?> postVoteOnPoll(@RequestBody VoteDTO voteDTO){
        VoteDTO postedVote = pollService.postVoteOnPoll(voteDTO);
        return new ResponseEntity<>(postedVote, HttpStatus.CREATED);
    }

    @GetMapping("/poll/{id}")
    public ResponseEntity<?> getPollById(@PathVariable("id") Long pollId){
        PollDetailsDTO pollDetailsDTO = pollService.getPollById(pollId);
        return new ResponseEntity<>(pollDetailsDTO, HttpStatus.OK);
    }
}
