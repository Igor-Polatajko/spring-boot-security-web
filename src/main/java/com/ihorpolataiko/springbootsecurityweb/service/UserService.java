package com.ihorpolataiko.springbootsecurityweb.service;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserCreateRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserPasswordUpdateRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserResponse;
import com.ihorpolataiko.springbootsecurityweb.dto.user.UserUpdateRequest;
import com.ihorpolataiko.springbootsecurityweb.entity.UserEntity;
import com.ihorpolataiko.springbootsecurityweb.mapper.UserMapper;
import com.ihorpolataiko.springbootsecurityweb.repository.UserRepository;
import com.ihorpolataiko.springbootsecurityweb.security.AuthUser;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public UserResponse createUser(UserCreateRequest userCreateRequest) {

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(userCreateRequest.username());
    userEntity.setPasswordHash(userCreateRequest.password()); // ToDo, convert to hash
    userEntity.setFirstName(userCreateRequest.firstName());
    userEntity.setLastName(userCreateRequest.lastName());
    userEntity.setRoles(List.of(Role.USER));
    userEntity.setActive(true);

    UserEntity savedEntity = userRepository.save(userEntity);

    return userMapper.toResponse(savedEntity);
  }

  public void changeUserPassword(
      UserPasswordUpdateRequest passwordUpdateRequest, AuthUser authUser) {

    UserEntity userEntity = getUserEntity(authUser.userId());

    // ToDo Ihor StringUtils
    if (!userEntity.getPasswordHash().equals(passwordUpdateRequest.oldPassword())) {
      throw new RuntimeException("Old password is incorrect");
    }

    userEntity.setPasswordHash(passwordUpdateRequest.newPassword());

    userRepository.save(userEntity);
  }

  public UserResponse updateUser(UserUpdateRequest userUpdateRequest, AuthUser authUser) {

    UserEntity userEntity = getUserEntity(authUser.userId());
    checkAccessToUser(authUser, userEntity);

    userEntity.setUsername(userUpdateRequest.username());
    userEntity.setFirstName(userUpdateRequest.firstName());
    userEntity.setLastName(userUpdateRequest.lastName());

    UserEntity updatedEntity = userRepository.save(userEntity);

    return userMapper.toResponse(updatedEntity);
  }

  public UserResponse getUser(String userId) {

    UserEntity userEntity = getUserEntity(userId);
    return userMapper.toResponse(userEntity);
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
    userEntity.setRoles(List.of(Role.USER, Role.ADMIN));

    UserEntity updatedEntity = userRepository.save(userEntity);
    return userMapper.toResponse(updatedEntity);
  }

  private UserEntity getUserEntity(String userId) {

    // ToDo use custom exception
    return userRepository.findById(userId).orElseThrow();
  }

  // for this method security responsibilities is scattered between controller and service
  private void checkAccessToUser(AuthUser authUser, UserEntity userEntity) {

    // ToDo use StringUtils.equals()
    if (authUser.role() != Role.ADMIN && !userEntity.getId().equals(authUser.userId())) {
      throw new RuntimeException("No access");
    }
  }
}
