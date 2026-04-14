package com.nazmul.polling.services.user;

import com.nazmul.polling.dto.PollDTO;

import java.util.List;

public interface PollService {
    PollDTO postPoll(PollDTO pollDTO);
    void deletePollById(Long id);
    List<PollDTO> getAllPolls();
    List<PollDTO> getMyPolls();
}
