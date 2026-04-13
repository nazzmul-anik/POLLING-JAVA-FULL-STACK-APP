package com.nazmul.polling.services.email;

import com.nazmul.polling.entity.Poll;
import com.nazmul.polling.entity.User;

public interface EmailService {
    void sendPollCreatedEmail(User user, Poll poll);
}
