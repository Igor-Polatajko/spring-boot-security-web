package com.ihorpolataiko.springbootsecurityweb.repository;

import com.ihorpolataiko.springbootsecurityweb.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, String> {

  Page<ItemEntity> findByUserId(String userId, Pageable pageable);
}
