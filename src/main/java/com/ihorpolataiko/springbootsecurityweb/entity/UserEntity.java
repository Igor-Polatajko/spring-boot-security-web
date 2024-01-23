package com.ihorpolataiko.springbootsecurityweb.entity;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

// ToDo created and updated dates
@Data
@Entity
public class UserEntity {

  @Id private String id;

  private String username;

  private String passwordHash;

  private String firstName;

  private String lastName;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER)
  private List<Role> roles;

  private Boolean active;

  private ZonedDateTime createdDate;

  private ZonedDateTime updatedDate;
}
