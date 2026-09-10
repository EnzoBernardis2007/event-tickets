package com.enzo.event.user.dto;

import com.enzo.event.user.User;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserResponse(
        UUID id,
        String name,
        String email,
        boolean verifiedEmail,
        Set<String> roles,
        Instant createdAt
) {
    public static UserResponse fromEntity(User user) {
        Set<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.isVerifiedEmail(),
                roles,
                user.getCreatedAt()
        );
    }
}