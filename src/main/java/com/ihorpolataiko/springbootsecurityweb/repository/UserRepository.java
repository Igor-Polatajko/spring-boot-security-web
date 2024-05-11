package com.ihorpolataiko.springbootsecurityweb.repository;

import com.ihorpolataiko.springbootsecurityweb.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, String> {

  Optional<UserEntity> findByUsername(String username);

  @Query(
      """
      SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u
          WHERE com.ihorpolataiko.springbootsecurityweb.common.Role.ROLE_ADMIN MEMBER OF u.roles
      """)
  boolean isAnyAdminExist();
}
