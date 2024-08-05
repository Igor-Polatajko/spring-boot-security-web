package com.ihorpolataiko.springbootsecurityweb.service;

import com.ihorpolataiko.springbootsecurityweb.common.ItemState;
import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemResponse;
import com.ihorpolataiko.springbootsecurityweb.entity.ItemEntity;
import com.ihorpolataiko.springbootsecurityweb.exception.NoAccessException;
import com.ihorpolataiko.springbootsecurityweb.exception.NotFoundException;
import com.ihorpolataiko.springbootsecurityweb.mapper.ItemMapper;
import com.ihorpolataiko.springbootsecurityweb.repository.ItemRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  private final ItemRepository itemRepository;

  private final ItemMapper itemMapper;

  public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
    this.itemRepository = itemRepository;
    this.itemMapper = itemMapper;
  }

  public ItemResponse createItem(ItemRequest itemRequest, OAuth2User authUser) {

    ItemEntity itemEntity = new ItemEntity();
    itemEntity.setData(itemRequest.data());
    itemEntity.setUserId(authUser.getName());
    itemEntity.setItemState(ItemState.CREATED);

    ItemEntity savedEntity = itemRepository.save(itemEntity);

    return itemMapper.toResponse(savedEntity);
  }

  public ItemResponse getItem(String itemId, OAuth2User authUser) {

    ItemEntity itemEntity = getItemEntity(itemId);
    checkIsOwner(authUser, itemEntity);

    return itemMapper.toResponse(itemEntity);
  }

  public Page<ItemResponse> listAllItems(Pageable pageable) {

    return itemRepository.findAll(pageable).map(itemMapper::toResponse);
  }

  public Page<ItemResponse> listUserItems(String userId, Pageable pageable) {

    return itemRepository.findByUserId(userId, pageable).map(itemMapper::toResponse);
  }

  public ItemResponse updateItem(String itemId, ItemRequest itemRequest, OAuth2User authUser) {

    ItemEntity itemEntity = getItemEntity(itemId);
    checkIsOwner(authUser, itemEntity);

    itemEntity.setData(itemRequest.data());
    itemEntity.setItemState(ItemState.CHANGED);

    ItemEntity updatedEntity = itemRepository.save(itemEntity);

    return itemMapper.toResponse(updatedEntity);
  }

  public void deleteItem(String itemId, OAuth2User authUser) {

    ItemEntity itemEntity = getItemEntity(itemId);
    checkIsOwner(authUser, itemEntity);
    itemRepository.deleteById(itemEntity.getId());
  }

  public ItemResponse approveItem(String itemId, OAuth2User authUser) {

    return setItemState(itemId, ItemState.APPROVED, authUser);
  }

  public ItemResponse rejectItem(String itemId, OAuth2User authUser) {

    return setItemState(itemId, ItemState.REJECTED, authUser);
  }

  private ItemResponse setItemState(String itemId, ItemState approved, OAuth2User authUser) {
    ItemEntity itemEntity = getItemEntity(itemId);

    checkIsOwner(authUser, itemEntity);

    itemEntity.setItemState(approved);

    ItemEntity updatedEntity = itemRepository.save(itemEntity);

    return itemMapper.toResponse(updatedEntity);
  }

  private ItemEntity getItemEntity(String itemId) {

    return itemRepository.findById(itemId).orElseThrow(NotFoundException::new);
  }

  private void checkIsOwner(OAuth2User authUser, ItemEntity itemEntity) {
    if (!isOwner(authUser, itemEntity)) {
      throw new NoAccessException();
    }
  }

  private boolean isOwner(OAuth2User authUser, ItemEntity itemEntity) {
    return StringUtils.equals(itemEntity.getUserId(), authUser.getName());
  }
}
