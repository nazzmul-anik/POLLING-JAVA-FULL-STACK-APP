package com.nazmul.polling.services.user;

import com.nazmul.polling.dto.*;

import java.util.List;

public interface PollService {
    PollDTO postPoll(PollDTO pollDTO);
    void deletePollById(Long id);
    List<PollDTO> getAllPolls();
    List<PollDTO> getMyPolls();
    LikeDTO giveLikeToPoll(Long pollId);
    CommentDTO postCommentToPoll(CommentDTO commentDTO);
    VoteDTO postVoteOnPoll(VoteDTO voteDTO);
    PollDetailsDTO getPollById(Long pollId);
}
