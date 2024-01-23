package com.ihorpolataiko.springbootsecurityweb.mapper;

import com.ihorpolataiko.springbootsecurityweb.dto.user.UserResponse;
import com.ihorpolataiko.springbootsecurityweb.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserResponse toResponse(UserEntity userEntity);
}
