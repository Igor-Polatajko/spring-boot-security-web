package com.ihorpolataiko.springbootsecurityweb.service;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import com.ihorpolataiko.springbootsecurityweb.dto.user.*;
import com.ihorpolataiko.springbootsecurityweb.entity.UserEntity;
import com.ihorpolataiko.springbootsecurityweb.exception.NotFoundException;
import com.ihorpolataiko.springbootsecurityweb.mapper.UserMapper;
import com.ihorpolataiko.springbootsecurityweb.repository.UserRepository;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public UserResponse syncUser(UserSyncRequest userSyncRequest) {

    UserEntity userEntity =
        userRepository
            .findById(userSyncRequest.userId())
            .orElseGet(
                () -> {
                  UserEntity newUserEntity = new UserEntity();
                  newUserEntity.setId(userSyncRequest.userId());
                  newUserEntity.setUsername(userSyncRequest.username());
                  newUserEntity.setFirstName(userSyncRequest.firstName());
                  newUserEntity.setLastName(userSyncRequest.lastName());
                  newUserEntity.setRoles(Set.of(Role.ROLE_USER));
                  newUserEntity.setActive(true);
                  return userRepository.save(newUserEntity);
                });

    return userMapper.toResponse(userEntity);
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

  private UserEntity getUserEntity(String userId) {

    return userRepository.findById(userId).orElseThrow(NotFoundException::new);
  }
}
