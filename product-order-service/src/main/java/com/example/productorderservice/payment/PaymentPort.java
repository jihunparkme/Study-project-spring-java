package com.example.productorderservice.payment;

import com.example.productorderservice.order.Order;

interface PaymentPort {

    Order getOrder(long orderId);

    void pay(int totalPrice, String cardNumber);

    void save(Payment payment);

}
