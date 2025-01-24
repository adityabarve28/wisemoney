package com.app.wisemoney.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.wisemoney.entity.UserEntity;
import com.app.wisemoney.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public UserEntity addUser(UserEntity user) {
		return userRepository.save(user);
	}
	 // Method to get user by username or email
    public Optional<UserEntity> getUserByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }
    public boolean userExists(String username, String email) {
        return userRepository.existsByUsername(username) || userRepository.existsByEmail(email);
    }
    
    public Optional<UserEntity> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

}