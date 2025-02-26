package com.listeningraccoon.pamietaj_o_zdrowiu.backend.services;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Sender {
    @Autowired
    private JavaMailSender sender;

    public void sendEmail(User user, Prescription prescription) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom("automated.email.kpr@gmail.com");
        message.setSubject("New prescription");

        String body = "A new prescription was created\n";
        body += prescription.toString();

        message.setText(body);
        sender.send(message);
    }


}
