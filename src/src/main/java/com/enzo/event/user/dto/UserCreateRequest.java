package com.enzo.event.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank()
        @Size(max = 120)
        String name,

        @NotBlank()
        @Email()
        @Size(max = 320)
        String email,

        @NotBlank()
        @Size(min = 8)
        String password
) {}