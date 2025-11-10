package com.example.budget_tracker.service;

import com.example.budget_tracker.domain.User;
import com.example.budget_tracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User findOrCreateUser(Jwt jwt) {
        String auth0Id = jwt.getSubject();
        return userRepository.findByAuth0Id(auth0Id).orElseGet(() -> {
            String email = jwt.getClaimAsString("email");
            String name = jwt.getClaimAsString("name");
            User newUser = new User(auth0Id, email, name);
            return userRepository.save(newUser);
        });
    }

    public User findByAuth0Id(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id).orElse(null);
    }
}
