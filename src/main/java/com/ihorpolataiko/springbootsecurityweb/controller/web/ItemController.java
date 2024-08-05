package com.ihorpolataiko.springbootsecurityweb.controller.web;

import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemResponse;
import com.ihorpolataiko.springbootsecurityweb.service.ItemService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/items")
public class ItemController {

  private final ItemService itemService;

  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping("/my")
  public String userHome(
      Model model, @AuthenticationPrincipal OAuth2User authUser, Pageable pageable) {
    model.addAttribute("items", itemService.listUserItems(authUser.getName(), pageable));
    return "items/myItems";
  }

  @GetMapping("/new")
  public String newItemPage(Model model, ItemRequest itemRequest) {
    model.addAttribute("itemRequest", itemRequest);
    return "items/new";
  }

  @GetMapping("/edit/{id}")
  public String editItemPage(
      @PathVariable("id") String itemId,
      @AuthenticationPrincipal OAuth2User authUser,
      Model model) {
    ItemResponse itemResponse = itemService.getItem(itemId, authUser);
    model.addAttribute("itemRequest", itemResponse);
    return "items/edit";
  }

  @PostMapping("/create")
  public String createItem(
      @ModelAttribute ItemRequest itemRequest, @AuthenticationPrincipal OAuth2User authUser) {
    itemService.createItem(itemRequest, authUser);
    return "redirect:/items/my";
  }

  @PostMapping("/update/{id}")
  public String updateItem(
      @PathVariable("id") String itemId,
      @AuthenticationPrincipal OAuth2User authUser,
      ItemRequest itemRequest) {
    itemService.updateItem(itemId, itemRequest, authUser);
    return "redirect:/items/my";
  }

  @PostMapping("/delete/{id}")
  public String deleteItem(
      @PathVariable("id") String itemId,
      @AuthenticationPrincipal OAuth2User authUser,
      @RequestHeader("referer") String refererHeader) {
    itemService.deleteItem(itemId, authUser);

    // Use referer header to redirect back, because delete can be invoked both from admin page and
    // from user items page
    return "redirect:" + refererHeader;
  }

  @PostMapping("/approve/{id}")
  public String approveItem(
      @PathVariable("id") String itemId, @AuthenticationPrincipal OAuth2User authUser) {
    itemService.approveItem(itemId, authUser);
    return "redirect:/items/my";
  }

  @PostMapping("/reject/{id}")
  public String rejectItem(
      @PathVariable("id") String itemId, @AuthenticationPrincipal OAuth2User authUser) {
    itemService.rejectItem(itemId, authUser);
    return "redirect:/items/my";
  }
}
