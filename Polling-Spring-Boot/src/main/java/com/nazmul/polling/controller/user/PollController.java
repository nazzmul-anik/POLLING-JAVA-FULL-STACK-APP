package com.nazmul.polling.controller.user;

import com.nazmul.polling.dto.PollDTO;
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
        return new ResponseEntity<>(creatdPollDTO, HttpStatus.OK);
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
}
