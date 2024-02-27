package com.example.productorderservice.order.application.port;

import com.example.productorderservice.order.domain.Order;
import com.example.productorderservice.product.domain.Product;

public interface OrderPort {
    Product getProductById(final long productId);

    void save(final Order order);
}
