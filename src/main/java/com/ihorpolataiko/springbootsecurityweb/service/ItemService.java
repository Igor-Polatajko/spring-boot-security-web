package com.ihorpolataiko.springbootsecurityweb.service;

import com.ihorpolataiko.springbootsecurityweb.common.ItemState;
import com.ihorpolataiko.springbootsecurityweb.common.Role;
import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemResponse;
import com.ihorpolataiko.springbootsecurityweb.entity.ItemEntity;
import com.ihorpolataiko.springbootsecurityweb.exception.NoAccessException;
import com.ihorpolataiko.springbootsecurityweb.exception.NotFoundException;
import com.ihorpolataiko.springbootsecurityweb.mapper.ItemMapper;
import com.ihorpolataiko.springbootsecurityweb.repository.ItemRepository;
import com.ihorpolataiko.springbootsecurityweb.security.AuthUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  private final ItemRepository itemRepository;

  private final ItemMapper itemMapper;

  public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
    this.itemRepository = itemRepository;
    this.itemMapper = itemMapper;
  }

  public ItemResponse createItem(ItemRequest itemRequest, AuthUser authUser) {

    ItemEntity itemEntity = new ItemEntity();
    itemEntity.setData(itemRequest.data());
    itemEntity.setUserId(authUser.userId());
    itemEntity.setItemState(ItemState.CREATED);

    ItemEntity savedEntity = itemRepository.save(itemEntity);

    return itemMapper.toResponse(savedEntity);
  }

  public ItemResponse getItem(String itemId, AuthUser authUser) {

    ItemEntity itemEntity = getItemEntity(itemId);
    checkAccessToItem(authUser, itemEntity);

    return itemMapper.toResponse(itemEntity);
  }

  public Page<ItemResponse> listAllItems(Pageable pageable) {

    return itemRepository.findAll(pageable).map(itemMapper::toResponse);
  }

  public Page<ItemResponse> listUserItems(String userId, Pageable pageable) {

    return itemRepository.findByUserId(userId, pageable).map(itemMapper::toResponse);
  }

  public ItemResponse updateItem(String itemId, ItemRequest itemRequest, AuthUser authUser) {

    ItemEntity itemEntity = getItemEntity(itemId);
    checkAccessToItem(authUser, itemEntity);

    itemEntity.setData(itemRequest.data());
    itemEntity.setItemState(ItemState.CHANGED);

    ItemEntity updatedEntity = itemRepository.save(itemEntity);

    return itemMapper.toResponse(updatedEntity);
  }

  public void deleteItem(String itemId, AuthUser authUser) {

    ItemEntity itemEntity = getItemEntity(itemId);
    checkAccessToItem(authUser, itemEntity);
    itemRepository.deleteById(itemEntity.getId());
  }

  public ItemResponse approveItem(String itemId) {

    return setItemState(itemId, ItemState.APPROVED);
  }

  public ItemResponse rejectItem(String itemId) {

    return setItemState(itemId, ItemState.REJECTED);
  }

  private ItemResponse setItemState(String itemId, ItemState approved) {
    ItemEntity itemEntity = getItemEntity(itemId);
    itemEntity.setItemState(approved);

    ItemEntity updatedEntity = itemRepository.save(itemEntity);

    return itemMapper.toResponse(updatedEntity);
  }

  private ItemEntity getItemEntity(String itemId) {

    return itemRepository.findById(itemId).orElseThrow(NotFoundException::new);
  }

  // for this method security responsibilities is scattered between controller and service
  private void checkAccessToItem(AuthUser authUser, ItemEntity itemEntity) {

    if (authUser.role() != Role.ADMIN
        && !StringUtils.equals(itemEntity.getUserId(), authUser.userId())) {
      throw new NoAccessException();
    }
  }
}
