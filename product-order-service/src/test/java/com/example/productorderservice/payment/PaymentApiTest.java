package com.example.productorderservice.payment;

import com.example.productorderservice.order.OrderSteps;
import com.example.productorderservice.product.ApiTest;
import com.example.productorderservice.product.ProductSteps;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentApiTest extends ApiTest {

    @Test
    void 상품주문() {
        ProductSteps.requestAddProduct(ProductSteps.getAddProductRequest());
        OrderSteps.requestCreateOrder(OrderSteps.getCreateOrderRequest());
        final var request = PaymentSteps.getPaymentRequest();

        final var response = PaymentSteps.requestPayment(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
