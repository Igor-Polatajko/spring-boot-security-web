package com.ihorpolataiko.springbootsecurityweb.service;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import com.ihorpolataiko.springbootsecurityweb.dto.user.*;
import com.ihorpolataiko.springbootsecurityweb.entity.UserEntity;
import com.ihorpolataiko.springbootsecurityweb.exception.NotFoundException;
import com.ihorpolataiko.springbootsecurityweb.mapper.UserMapper;
import com.ihorpolataiko.springbootsecurityweb.repository.UserRepository;
import com.ihorpolataiko.springbootsecurityweb.security.exception.ApplicationAuthenticationException;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;

import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {


  private final String defaultAdminUsername;

  private final String defaultAdminPassword;

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  private final PasswordEncoder passwordEncoder;

  public UserService(
          @Value("${admin.default.username}") String defaultAdminUsername,
          @Value("${admin.default.password}") String defaultAdminPassword,
          UserRepository userRepository,
          UserMapper userMapper,
          PasswordEncoder passwordEncoder) {
      this.defaultAdminUsername = defaultAdminUsername;
      this.defaultAdminPassword = defaultAdminPassword;
      this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
  }

  public UserResponse createUser(UserCreateRequest userCreateRequest) {

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(userCreateRequest.username());
    userEntity.setPasswordHash(passwordEncoder.encode(userCreateRequest.password()));
    userEntity.setFirstName(userCreateRequest.firstName());
    userEntity.setLastName(userCreateRequest.lastName());
    userEntity.setRoles(Set.of(Role.ROLE_USER));
    userEntity.setActive(true);

    UserEntity savedEntity = userRepository.save(userEntity);

    return userMapper.toResponse(savedEntity);
  }

  public void changeUserPassword(
      UserPasswordUpdateRequest passwordUpdateRequest, AuthUser authUser) {

    UserEntity userEntity = getUserEntity(authUser.userId());

    if (!passwordEncoder.matches(
        passwordUpdateRequest.oldPassword(), userEntity.getPasswordHash())) {
      throw new ApplicationAuthenticationException("Old password is incorrect");
    }

    userEntity.setPasswordHash(passwordEncoder.encode(passwordUpdateRequest.newPassword()));

    userRepository.save(userEntity);
  }

  public UserResponse updateUser(String userId, UserUpdateRequest userUpdateRequest) {

    UserEntity userEntity = getUserEntity(userId);

    userEntity.setUsername(userUpdateRequest.username());
    userEntity.setFirstName(userUpdateRequest.firstName());
    userEntity.setLastName(userUpdateRequest.lastName());

    UserEntity updatedEntity = userRepository.save(userEntity);

    return userMapper.toResponse(updatedEntity);
  }

  public UserResponse getUserById(String userId) {

    UserEntity userEntity = getUserEntity(userId);
    return userMapper.toResponse(userEntity);
  }

  public UserResponseWithCredentials getUserCredentialsByUsername(String username) {

    UserEntity userEntity =
        userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    return new UserResponseWithCredentials(
        userMapper.toResponse(userEntity), userEntity.getPasswordHash());
  }

  public Page<UserResponse> listUsers(Pageable pageable) {

    return userRepository.findAll(pageable).map(userMapper::toResponse);
  }

  public UserResponse activateUser(String userId) {

    UserEntity userEntity = getUserEntity(userId);
    userEntity.setActive(true);

    UserEntity updatedEntity = userRepository.save(userEntity);
    return userMapper.toResponse(updatedEntity);
  }

  public UserResponse deactivateUser(String userId) {

    UserEntity userEntity = getUserEntity(userId);
    userEntity.setActive(false);

    UserEntity updatedEntity = userRepository.save(userEntity);
    return userMapper.toResponse(updatedEntity);
  }

  public UserResponse promoteUserToAdmin(String userId) {

    UserEntity userEntity = getUserEntity(userId);
    userEntity.getRoles().add(Role.ROLE_ADMIN);

    UserEntity updatedEntity = userRepository.save(userEntity);
    return userMapper.toResponse(updatedEntity);
  }

  public void createDefaultAdminIfNotExist() {

    boolean anyAdminExist = userRepository.isAnyAdminExist();

    if(anyAdminExist) {
      log.info("Admin already exist. Skipping creation of default admin user");
      return;
    }

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(defaultAdminUsername);
    userEntity.setPasswordHash(passwordEncoder.encode(defaultAdminPassword));
    userEntity.setFirstName("Admin");
    userEntity.setLastName("Admin");
    userEntity.setRoles(Set.of(Role.ROLE_ADMIN));
    userEntity.setActive(true);

    UserEntity savedUser = userRepository.save(userEntity);

    log.info("Default admin user was created with id={}", savedUser.getId());
  }

  private UserEntity getUserEntity(String userId) {

    return userRepository.findById(userId).orElseThrow(NotFoundException::new);
  }
}
