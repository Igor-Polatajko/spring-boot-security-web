package com.ihorpolataiko.springbootsecurityweb.mapper;

import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemResponse;
import com.ihorpolataiko.springbootsecurityweb.entity.ItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

  ItemResponse toResponse(ItemEntity itemEntity);
}
