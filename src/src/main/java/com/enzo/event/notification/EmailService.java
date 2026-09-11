package com.enzo.event.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmailVerification(
            String email,
            String name,
            String token
    ) {

        String verificationUrl =
                "http://localhost:8080/auth/verify-email?token="
                        + URLEncoder.encode(
                                token,
                                StandardCharsets.UTF_8
                        );

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Confirm your email");
        message.setText("""
                Hello %s,

                Please confirm your email address by clicking the link below:

                %s

                This link expires in 24 hours.

                If you did not create an account, you can ignore this email.
                """.formatted(name, verificationUrl));

        mailSender.send(message);
    }
}