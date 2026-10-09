package com.poolo.poolo.identity.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.poolo.poolo.identity.dto.RegisterUserRequest;
import com.poolo.poolo.identity.dto.UserResponseDTO;
import com.poolo.poolo.identity.models.User;
import com.poolo.poolo.identity.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDTO registerDTO(RegisterUserRequest user){

        if ( userRepository.existsByCorporateEmail(user.corporateEmail())){
            throw new IllegalArgumentException("Email already registered");
        }
        
        User newUser = User.builder()
                            .name(user.name())
                            .corporateEmail(user.corporateEmail())
                            .phoneNumber(user.phoneNumber())
                            .role(user.role())
                            .passwordHash(user.password()) 
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


}
