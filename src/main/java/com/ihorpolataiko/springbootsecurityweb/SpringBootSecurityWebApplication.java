package com.ihorpolataiko.springbootsecurityweb;

import jakarta.servlet.ServletContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.context.ServletContextAware;

@SpringBootApplication
public class SpringBootSecurityWebApplication implements ServletContextAware {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootSecurityWebApplication.class, args);
  }

  @Override
  public void setServletContext(ServletContext servletContext) {
    System.out.println(servletContext.getFilterRegistrations());
  }
}
