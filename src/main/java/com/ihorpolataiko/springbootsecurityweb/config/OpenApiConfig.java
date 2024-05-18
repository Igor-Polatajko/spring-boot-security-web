package com.ihorpolataiko.springbootsecurityweb.config;

import com.ihorpolataiko.springbootsecurityweb.common.OpenApiConstants;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = OpenApiConstants.BASIC_SECURITY_REQUIREMENT,
    type = SecuritySchemeType.HTTP,
    scheme = "basic")
public class OpenApiConfig {}
