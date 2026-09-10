package com.enzo.event.user;

import com.enzo.event.user.dto.UserCreateRequest;
import com.enzo.event.user.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse createCustomer(UserCreateRequest userCreateRequest) {
        if (userRepository.findByEmailAndDeletedAtIsNull(userCreateRequest.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new IllegalStateException("CUSTOMER role not found"));

        User user = new User();
        user.setName(userCreateRequest.name());
        user.setEmail(userCreateRequest.email());
        user.setVerifiedEmail(false);
        user.setPasswordHash(passwordEncoder.encode(userCreateRequest.password()));

        user.addRole(customerRole);

        User savedUser = userRepository.save(user);

        return UserResponse.fromEntity(savedUser);
    }
}