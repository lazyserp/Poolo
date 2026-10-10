package com.poolo.poolo.identity.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poolo.poolo.identity.models.EmailVerificationToken;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken,UUID>{
    Optional<EmailVerificationToken> findByCorporateEmail(String corporateEmail);

}
