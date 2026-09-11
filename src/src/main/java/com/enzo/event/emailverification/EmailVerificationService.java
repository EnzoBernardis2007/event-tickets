package com.enzo.event.emailverification;

import com.enzo.event.user.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final VerificationTokenGenerator tokenGenerator;
    private final TokenHasher tokenHasher;

    public EmailVerificationService(
            EmailVerificationTokenRepository tokenRepository,
            VerificationTokenGenerator tokenGenerator,
            TokenHasher tokenHasher
    ) {
        this.tokenRepository = tokenRepository;
        this.tokenGenerator = tokenGenerator;
        this.tokenHasher = tokenHasher;
    }

    public String createToken(User user) {

        String rawToken = tokenGenerator.generate();

        EmailVerificationToken token = new EmailVerificationToken();

        token.setUser(user);
        token.setTokenHash(tokenHasher.hash(rawToken));
        token.setExpiresAt(LocalDateTime.now().plusHours(24));

        tokenRepository.save(token);

        return rawToken;
    }

    @Transactional
    public void verify(String rawToken) {

        String tokenHash = tokenHasher.hash(rawToken);

        EmailVerificationToken token =
                tokenRepository.findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid verification token"
                                )
                        );

        if (token.isUsed()) {
            throw new IllegalArgumentException(
                    "Verification token already used"
            );
        }

        if (token.isExpired()) {
            throw new IllegalArgumentException(
                    "Verification token expired"
            );
        }

        User user = token.getUser();

        user.setVerifiedEmail(true);

        token.setUsedAt(LocalDateTime.now());
    }
}