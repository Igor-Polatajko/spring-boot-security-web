package com.ihorpolataiko.springbootsecurityweb.controller.api;

import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemResponse;
import com.ihorpolataiko.springbootsecurityweb.service.ItemService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
public class ItemApiController {

  private final ItemService itemService;

  public ItemApiController(ItemService itemService) {
    this.itemService = itemService;
  }

  @PostMapping
  public ItemResponse createItem(
      @RequestBody ItemRequest itemRequest, @AuthenticationPrincipal OAuth2User authUser) {
    return itemService.createItem(itemRequest, authUser);
  }

  @GetMapping("/{id}")
  public ItemResponse getItem(
      @PathVariable("id") String itemId, @AuthenticationPrincipal OAuth2User authUser) {
    return itemService.getItem(itemId, authUser);
  }

  @GetMapping
  public Page<ItemResponse> listAllItems(@PageableDefault @ParameterObject Pageable pageable) {
    return itemService.listAllItems(pageable);
  }

  @GetMapping(params = "userId")
  public Page<ItemResponse> listUserItems(
      @Parameter(allowEmptyValue = true) @RequestParam("userId") String userId,
      @PageableDefault @ParameterObject Pageable pageable) {
    return itemService.listUserItems(userId, pageable);
  }

  @PutMapping("/{id}")
  public ItemResponse updateItem(
      @PathVariable("id") String itemId,
      @RequestBody ItemRequest itemRequest,
      @AuthenticationPrincipal OAuth2User authUser) {
    return itemService.updateItem(itemId, itemRequest, authUser);
  }

  @DeleteMapping("/{id}")
  public void deleteItem(
      @PathVariable("id") String itemId, @AuthenticationPrincipal OAuth2User authUser) {
    itemService.deleteItem(itemId, authUser);
  }

  @PostMapping("/{id}/approve")
  public ItemResponse approveItem(
      @PathVariable("id") String itemId, @AuthenticationPrincipal OAuth2User authUser) {
    return itemService.approveItem(itemId, authUser);
  }

  @PostMapping("/{id}/reject")
  public ItemResponse rejectItem(
      @PathVariable("id") String itemId, @AuthenticationPrincipal OAuth2User authUser) {
    return itemService.rejectItem(itemId, authUser);
  }
}
