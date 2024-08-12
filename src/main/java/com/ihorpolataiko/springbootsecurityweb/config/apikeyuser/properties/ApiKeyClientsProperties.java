package com.ihorpolataiko.springbootsecurityweb.config.apikeyuser.properties;

import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api-key")
@Data
public class ApiKeyClientsProperties {

  private Map<String, String> clients;
}
