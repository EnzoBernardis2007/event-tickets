package com.enzo.event.auth;

import com.enzo.event.user.UserService;
import com.enzo.event.user.dto.UserCreateRequest;
import com.enzo.event.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v0/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody UserCreateRequest userCreateRequest
    ) {
        UserResponse userResponse = userService.createCustomer(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}