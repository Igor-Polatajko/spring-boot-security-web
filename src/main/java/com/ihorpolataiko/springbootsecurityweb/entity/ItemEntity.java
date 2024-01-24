package com.ihorpolataiko.springbootsecurityweb.entity;

import com.ihorpolataiko.springbootsecurityweb.common.ItemState;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

// ToDo created and updated dates
@Data
@Entity
public class ItemEntity {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;

  private String data;

  private String userId;

  private ItemState itemState;

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
