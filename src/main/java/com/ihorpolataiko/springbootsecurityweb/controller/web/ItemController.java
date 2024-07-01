package com.ihorpolataiko.springbootsecurityweb.controller.web;

import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemRequest;
import com.ihorpolataiko.springbootsecurityweb.dto.item.ItemResponse;
import com.ihorpolataiko.springbootsecurityweb.security.user.AuthUser;
import com.ihorpolataiko.springbootsecurityweb.service.ItemService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/items")
public class ItemController {

  private final ItemService itemService;

  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/my")
  public String userHome(
      Model model, @AuthenticationPrincipal AuthUser authUser, Pageable pageable) {
    model.addAttribute("items", itemService.listUserItems(authUser.userId(), pageable));
    return "items/myItems";
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/admin")
  public String adminPage(Model model, Pageable pageable) {
    model.addAttribute("items", itemService.listAllItems(pageable));
    return "items/admin";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/new")
  public String newItemPage(Model model, ItemRequest itemRequest) {
    model.addAttribute("itemRequest", itemRequest);
    return "items/new";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/edit/{id}")
  public String editItemPage(
      @PathVariable("id") String itemId, @AuthenticationPrincipal AuthUser authUser, Model model) {
    ItemResponse itemResponse = itemService.getItem(itemId, authUser);
    model.addAttribute("itemRequest", itemResponse);
    return "items/edit";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create")
  public String createItem(
      @ModelAttribute ItemRequest itemRequest, @AuthenticationPrincipal AuthUser authUser) {
    itemService.createItem(itemRequest, authUser);
    return "redirect:/items/my";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/update/{id}")
  public String updateItem(
      @PathVariable("id") String itemId,
      @AuthenticationPrincipal AuthUser authUser,
      ItemRequest itemRequest) {
    itemService.updateItem(itemId, itemRequest, authUser);
    return "redirect:/items/my";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/delete/{id}")
  public String deleteItem(
      @PathVariable("id") String itemId,
      @AuthenticationPrincipal AuthUser authUser,
      @RequestHeader("referer") String refererHeader) {
    itemService.deleteItem(itemId, authUser);

    // Use referer header to redirect back, because delete can be invoked both from admin page and
    // from user items page
    return "redirect:" + refererHeader;
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/approve/{id}")
  public String approveItem(@PathVariable("id") String itemId) {
    itemService.approveItem(itemId);
    return "redirect:/items/admin";
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/reject/{id}")
  public String rejectItem(@PathVariable("id") String itemId) {
    itemService.rejectItem(itemId);
    return "redirect:/items/admin";
  }
}
