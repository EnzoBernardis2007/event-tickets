package com.enzo.event.notification;

import com.enzo.event.user.event.UserRegisteredEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserRegisteredEventHandler {

    private final EmailService emailService;

    public UserRegisteredEventHandler(
            EmailService emailService
    ) {
        this.emailService = emailService;
    }

    @TransactionalEventListener
    public void handle(UserRegisteredEvent event) {
        emailService.sendEmailVerification(
                event.email(),
                event.verificationToken()
        );
    }
}