package com.example.productorderservice.order;

import com.example.productorderservice.product.Product;

interface OrderPort {
    Product getProductById(final long productId);

    void save(final Order order);
}
