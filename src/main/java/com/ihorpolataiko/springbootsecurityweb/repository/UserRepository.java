package com.ihorpolataiko.springbootsecurityweb.repository;

import com.ihorpolataiko.springbootsecurityweb.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

  Optional<UserEntity> findByUsername(String username);
}
