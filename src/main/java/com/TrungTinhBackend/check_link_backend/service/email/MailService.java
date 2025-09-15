package com.TrungTinhBackend.check_link_backend.service.email;

import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public APIResponse sendEmail(String to, String subject, String body) {
        APIResponse apiResponse = new APIResponse();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Sending mail success !");
        apiResponse.setData(message);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
