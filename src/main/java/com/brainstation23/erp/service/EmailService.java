package com.brainstation23.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

//    private final JavaMailSender javaMailSender;
//
//    public void SendMail(String toEmail, String subject, String body){
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("abir835.bs23@gmail.com");
//        message.setTo(toEmail);
//        message.setText(body);
//        message.setSubject(subject);
//        javaMailSender.send(message);
//        System.out.println("mail send successfully");
//    }
}
