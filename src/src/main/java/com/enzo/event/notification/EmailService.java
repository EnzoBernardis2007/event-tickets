package com.enzo.event.notification;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    public EmailService(Resend resend) {
        this.resend = resend;
    }

    public void sendEmailVerification(
            String email,
            String verificationToken
    ) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to(email)
                .subject("Verify your email")
                .html("""
                        <h1>Verify your email</h1>
                        <p>Click the link below to verify your email:</p>
                        <a href="http://localhost:8080/auth/verify-email?token=%s">Verify email</a>
                        """.formatted(verificationToken))
                .build();

        try {
            resend.emails().send(params);
        } catch (ResendException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}