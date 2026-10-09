package com.poolo.poolo.identity.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poolo.poolo.identity.models.User;

@Repository 
public interface UserRepository extends JpaRepository<User,UUID>{

    public Optional<User> findByPhoneNumber(String phoneNumber);
    public Boolean existsByCorporateEmail(String email);
    public Optional<User> findByCorporateEmail(String email);
    
}
