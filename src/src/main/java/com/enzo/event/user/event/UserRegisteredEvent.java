package com.enzo.event.user.event;

import java.util.UUID;

public record UserRegisteredEvent(
        UUID userId,
        String email,
        String name,
        String verificationToken
) {
}