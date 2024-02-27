package com.example.productorderservice.order;

import java.util.HashMap;
import java.util.Map;

class OrderRepository {
    private Map<Long, Order> persistence = new HashMap<>();
    private Long sequance = 0L;

    public void save(final Order order) {
        order.assignId(++sequance);
        persistence.put(order.getId(), order);
    }
}
