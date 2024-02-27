package com.example.productorderservice.payment;

import com.example.productorderservice.order.OrderService;
import com.example.productorderservice.order.OrderSteps;
import com.example.productorderservice.product.ProductService;
import com.example.productorderservice.product.ProductSteps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaymentServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Test
    void 상품주문() {
        productService.addProduct(ProductSteps.getAddProductRequest());
        orderService.createOrder(OrderSteps.getCreateOrderRequest());
        final PaymentRequest request = PaymentSteps.getPaymentRequest();

        paymentService.payment(request);
    }
}
