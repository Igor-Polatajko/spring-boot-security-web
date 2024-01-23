package com.ihorpolataiko.springbootsecurityweb.entity;

import com.ihorpolataiko.springbootsecurityweb.common.ItemState;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.ZonedDateTime;
import lombok.Data;

// ToDo created and updated dates
@Data
@Entity
public class ItemEntity {

  @Id private String id;

  private String data;

  private String userId;

  private ItemState itemState;

  private ZonedDateTime createdDate;

  private ZonedDateTime updatedDate;
}
