package com.ihorpolataiko.springbootsecurityweb.dto.item;

import com.ihorpolataiko.springbootsecurityweb.common.ItemState;

public record ItemResponse(String id, String data, String userId, ItemState itemState) {}
