package com.nazmul.polling.services.user;

import com.nazmul.polling.dto.PollDTO;

public interface PollService {
    PollDTO postPoll(PollDTO pollDTO);
    void deletePollById(Long id);
}
