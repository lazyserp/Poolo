package com.poolo.poolo.identity.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poolo.poolo.identity.dto.AuthResponseDTO;
import com.poolo.poolo.identity.dto.ConfirmEmailVerificationRequest;
import com.poolo.poolo.identity.dto.LoginRequest;
import com.poolo.poolo.identity.dto.RegisterUserRequest;
import com.poolo.poolo.identity.dto.RequestEmailVerificationRequest;
import com.poolo.poolo.identity.dto.UserResponseDTO;
import com.poolo.poolo.identity.models.EmailVerificationToken;
import com.poolo.poolo.identity.models.Role;
import com.poolo.poolo.identity.models.User;
import com.poolo.poolo.identity.repository.EmailVerificationTokenRepository;
import com.poolo.poolo.identity.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationTokenRepository tokenRepository;
    private final JwtService jwtService;

    private static final Set<String> FORBIDDEN_DOMAINS  = Set.of("gmail.com","yahoo.com","outlook.com","hotmail.com","icloud.com");

    private void validCorporateDomain(String email){
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        if ( FORBIDDEN_DOMAINS.contains(domain)){
            throw new IllegalArgumentException("Personal Emails are not allowed , Please your official corporate email for verification");
        }
    }

    private String generateOtp(){
        return String.format("%06d", new java.security.SecureRandom().nextInt(1_000_000));
    }

    @Transactional 
    public void requestEmailVerification(RequestEmailVerificationRequest request){
        String email = request.corporateEmail();
        validCorporateDomain(email);

        User user = userRepository.findByCorporateEmail(email)
                                                    .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        if ( user.isCorporateEmailVerified()){
            throw new IllegalArgumentException("Email is Already verified");
        }

        String otp = generateOtp();
        Instant expiresAt = Instant.now().plus(10,ChronoUnit.MINUTES);

        // If a token already exists for this email, update it; otherwise create a new one
        EmailVerificationToken token = tokenRepository.findByCorporateEmail(email)
                                                            .orElseGet(() -> EmailVerificationToken.builder()
                                                                                             .corporateEmail(email)
                                                                                             .createdAt(Instant.now())
                                                                                             .build());
        token.setOtp(otp);
        token.setExpiresAt(expiresAt);
        token.setAttempts(0);
        tokenRepository.save(token);


        System.out.println("==================================================");
        System.out.println(" VERIFICATION OTP FOR " + email + ": " + otp);
        System.out.println("==================================================");
    }

    @Transactional 
    public void confirmEmailVerification(ConfirmEmailVerificationRequest request){
        String email = request.corporateEmail();

        EmailVerificationToken token = tokenRepository.findByCorporateEmail(email).orElseThrow(() -> new IllegalArgumentException("No verification Request found for this email"));

        if ( token.isMaxAttemptReached()){
            tokenRepository.delete(token);
            throw new IllegalArgumentException("Max Attempts reached, reqeust a new OTP");
        }

        if ( token.isExpired()){
            throw new IllegalArgumentException("OTP expired");
        }

        if ( !token.getOtp().equals(request.otp())){
            token.setAttempts(token.getAttempts()+1);
            tokenRepository.save(token);
            throw new IllegalArgumentException("Invalid OTP , try again");
        }

        User user = userRepository.findByCorporateEmail(email).orElseThrow(() -> new IllegalArgumentException("User not Found"));

        user.setCorporateEmail(email);
        user.setCorporateEmailVerified(true);
        tokenRepository.delete(token);
        userRepository.save(user);

    }

    

    public UserResponseDTO registerDTO(RegisterUserRequest user){
        
        if ( userRepository.existsByCorporateEmail(user.corporateEmail())){
            throw new IllegalArgumentException("Email already registered");
        }

        if ( user.role() == Role.ADMIN)
        {
            throw new IllegalArgumentException("Access Denied");
        }

        String hashedPassword = passwordEncoder.encode(user.password());
        
        User newUser = User.builder()
                            .name(user.name())
                            .corporateEmail(user.corporateEmail())
                            .phoneNumber(user.phoneNumber())
                            .role(user.role())
                            .passwordHash(hashedPassword) 
                            .createdAt(Instant.now())
                            .build();


        User savedUser = userRepository.save(newUser);

        return new UserResponseDTO(
                        savedUser.getId(),
                        savedUser.getName(),
                        savedUser.getCorporateEmail(),
                        savedUser.getPhoneNumber(),
                        savedUser.isCorporateEmailVerified(),
                        savedUser.getRole(),
                        savedUser.getCreatedAt()
                        );

    }

    public UserResponseDTO getUserById(UUID id){
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("User not find"));
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getCorporateEmail(),
            user.getPhoneNumber(),
            user.isCorporateEmailVerified(),
            user.getRole(),
            user.getCreatedAt()
        );
    }

        public AuthResponseDTO login(LoginRequest request){
            User user = userRepository.findByCorporateEmail(request.corporateEmail()).orElseThrow(() -> new IllegalArgumentException("No User Found"));

            if(!passwordEncoder.matches(request.password(), user.getPasswordHash())){
                throw new IllegalArgumentException("Invalid email or password");
            }


                String accessToken = jwtService.generateAccessToken(user.getId(), request.corporateEmail(), user.getRole());
                String refreshToken = jwtService.generateRefreshToken(user.getId());

                UserResponseDTO userDTO = new UserResponseDTO(
                    user.getId(),
                    user.getName(),
                    user.getCorporateEmail(),
                    user.getPhoneNumber(),
                    user.isCorporateEmailVerified(),
                    user.getRole(),
                    user.getCreatedAt()
                );

                return AuthResponseDTO.of(accessToken, refreshToken, userDTO);
        }
    }
