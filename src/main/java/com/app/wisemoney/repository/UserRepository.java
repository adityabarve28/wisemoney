package com.app.wisemoney.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.wisemoney.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>{
	 Optional<UserEntity> findByUsernameOrEmail(String username, String email);
	  boolean existsByUsername(String username);
	    boolean existsByEmail(String email);
}