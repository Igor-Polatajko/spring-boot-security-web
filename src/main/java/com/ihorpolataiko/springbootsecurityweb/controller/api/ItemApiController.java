package com.ihorpolataiko.springbootsecurityweb.controller.api;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemResponse;
import com.ihorpolataiko.springbootsecurityweb.security.AuthUser;
import com.ihorpolataiko.springbootsecurityweb.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemApiController {

  private final ItemService itemService;

  public ItemApiController(ItemService itemService) {
    this.itemService = itemService;
  }

  // Authenticated
  @PostMapping
  public ItemResponse createItem(ItemRequest itemRequest) {
    return itemService.createItem(itemRequest, new AuthUser("id", Role.USER));
  }

  // Authenticated
  @GetMapping("/{id}")
  public ItemResponse getItem(@PathVariable("id") String itemId) {
    return itemService.getItem(itemId, new AuthUser("id", Role.USER));
  }

  // Admin
  @GetMapping
  public Page<ItemResponse> listAllItems(Pageable pageable) {
    return itemService.listAllItems(pageable);
  }

  // Authenticated
  // ToDo check on security level or controller level, that id of the user matches the id in the url
  //  or the role is admin
  @GetMapping(params = "userId")
  public Page<ItemResponse> listUserItems(
      @RequestParam("userId") String userId, Pageable pageable) {
    return itemService.listUserItems(userId, pageable);
  }

  // Authenticated
  @PutMapping("/{id}")
  public ItemResponse updateItem(@PathVariable("id") String itemId, ItemRequest itemRequest) {
    return itemService.updateItem(itemId, itemRequest, new AuthUser("id", Role.USER));
  }

  // Authenticated
  @DeleteMapping("/{id}")
  public void deleteItem(@PathVariable("id") String itemId) {
    itemService.deleteItem(itemId, new AuthUser("id", Role.USER));
  }

  // Admin
  @PostMapping("/{id}/approve")
  public ItemResponse approveItem(@PathVariable("id") String itemId) {
    return itemService.approveItem(itemId);
  }

  // Admin
  @PostMapping("/{id}/reject")
  public ItemResponse rejectItem(@PathVariable("id") String itemId) {
    return itemService.rejectItem(itemId);
  }
}
