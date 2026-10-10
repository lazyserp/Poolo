package com.poolo.poolo.identity.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poolo.poolo.identity.dto.ConfirmEmailVerificationRequest;
import com.poolo.poolo.identity.dto.RegisterUserRequest;
import com.poolo.poolo.identity.dto.RequestEmailVerificationRequest;
import com.poolo.poolo.identity.dto.UserResponseDTO;
import com.poolo.poolo.identity.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor 
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterUserRequest request){
        UserResponseDTO response = userService.registerDTO(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/verify-email/request")
    public ResponseEntity<Map<String,String>> requestVerification(@Valid @RequestBody RequestEmailVerificationRequest request){
        userService.requestEmailVerification(request);
        return ResponseEntity.ok(Map.of("message","Verification OTP sent to email."));
    }

    @PostMapping("/verify-email/confirm")
    public ResponseEntity<Map<String,String>> confirmVerification(@Valid @RequestBody ConfirmEmailVerificationRequest request){
        userService.confirmEmailVerification(request);
        return ResponseEntity.ok(Map.of("message","Email is verified successfully"));
    }

    
}
