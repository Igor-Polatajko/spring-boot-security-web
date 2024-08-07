package com.ihorpolataiko.springbootsecurityweb.repository;

import com.ihorpolataiko.springbootsecurityweb.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {}
