package com.ihorpolataiko.springbootsecurityweb.entity;

import com.ihorpolataiko.springbootsecurityweb.common.Role;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class UserEntity {

  @Id private String id;

  @Column(unique = true)
  private String username;

  private String firstName;

  private String lastName;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER)
  private Set<Role> roles;

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
