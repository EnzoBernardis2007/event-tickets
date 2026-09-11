package com.enzo.event.user;

import com.enzo.event.emailverification.EmailVerificationService;
import com.enzo.event.role.Role;
import com.enzo.event.role.RoleRepository;
import com.enzo.event.user.dto.UserCreateRequest;
import com.enzo.event.user.dto.UserResponse;
import com.enzo.event.user.event.UserRegisteredEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final ApplicationEventPublisher eventPublisher;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            EmailVerificationService emailVerificationService,
            ApplicationEventPublisher eventPublisher
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailVerificationService = emailVerificationService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UserResponse createCustomer(UserCreateRequest request) {

        if (userRepository
                .findByEmailAndDeletedAtIsNull(request.email())
                .isPresent()) {

            throw new IllegalArgumentException("Email already in use");
        }

        Role customerRole = roleRepository
                .findByName("CUSTOMER")
                .orElseThrow(() ->
                        new IllegalStateException("CUSTOMER role not found")
                );

        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setVerifiedEmail(false);
        user.setPasswordHash(
                passwordEncoder.encode(request.password())
        );

        user.addRole(customerRole);

        User savedUser = userRepository.save(user);

        String verificationToken =
                emailVerificationService.createToken(savedUser);

        eventPublisher.publishEvent(
                new UserRegisteredEvent(
                        savedUser.getId(),
                        savedUser.getEmail(),
                        savedUser.getName(),
                        verificationToken
                )
        );

        return UserResponse.fromEntity(savedUser);
    }
}