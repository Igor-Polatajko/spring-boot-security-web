package com.ihorpolataiko.springbootsecurityweb.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/no-access")
public class AccessDeniedController {

  @GetMapping
  public String accessDenied() {
    return "accessDenied";
  }
}
