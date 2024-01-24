package com.ihorpolataiko.springbootsecurityweb.entity;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
public class UserEntity {

  // ToDo find  strategy = "uuid" is deprecated
  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;

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

  @PrePersist
  public void onPrePersist() {

    createdDate = ZonedDateTime.now();
    updatedDate = ZonedDateTime.now();
  }

  @PreUpdate
  public void onPreUpdate() {

    updatedDate = ZonedDateTime.now();
  }
}
