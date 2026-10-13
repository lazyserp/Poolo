package com.poolo.poolo.identity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration 
@EnableWebSecurity 
@RequiredArgsConstructor 
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFiler;

    @Bean 
    public PasswordEncoder passswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/**").permitAll()
                                            .requestMatchers("/api/v1/trips/search").permitAll()
                                            .requestMatchers("/h2-console/**").permitAll()
                                            .anyRequest().authenticated())
        .headers(headers -> headers.frameOptions( HeadersConfigurer.FrameOptionsConfig::disable))
        .addFilterBefore(jwtAuthFiler, UsernamePasswordAuthenticationFilter.class);

        return  http.build();
    }
}
