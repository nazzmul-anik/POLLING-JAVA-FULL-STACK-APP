package com.nazmul.polling.services.email;

import com.nazmul.polling.entity.Poll;
import com.nazmul.polling.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    @Override
    public void sendPollCreatedEmail(User user, Poll poll) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("anik115807@gmail.com");
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject("New Poll Posted");
            mimeMessageHelper.setText(
                    "Dear "+ user.getFirstName() + "! I trust this message finds you in good spirits. I wanted to inform you that a new poll has been successfully posted." +
                            "The question you submitted is as follows: '"+poll.getQuestion() + "'." +
                            "This poll was posted on "+ poll.getPostedDate() + ", and it is scheduled to expire on " + poll.getPostedDate()+"." +
                            "Thank you for your engagement and contribution to our platform. POLL APP"
            );
            javaMailSender.send(mimeMessage);
            System.out.println("Mail Sent To : "+ user.getEmail());
        }catch(MessagingException e){
            System.err.println("Failed To Send Mail : " + e.getMessage());
        }
    }
}
