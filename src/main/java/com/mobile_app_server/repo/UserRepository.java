package com.mobile_app_server.repo;

import com.mobile_app_server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity getUserByUsername(String username);
}
