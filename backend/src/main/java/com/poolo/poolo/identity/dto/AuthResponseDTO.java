package com.poolo.poolo.identity.dto;

public record AuthResponseDTO(
    String accessToken,
    String refreshToken,
    String tokenType,
    UserResponseDTO user
){

public static AuthResponseDTO of(String accessToken, String refreshToken, UserResponseDTO user) {
        return new AuthResponseDTO(accessToken, refreshToken, "Bearer", user);
}
}