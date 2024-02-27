package com.example.productorderservice.payment;

public class PaymentSteps {
    public static PaymentRequest getPaymentRequest() {
        final long orderId = 1L;
        final String cardNumber = "1234-1234-1234";
        return new PaymentRequest(orderId, cardNumber);
    }
}