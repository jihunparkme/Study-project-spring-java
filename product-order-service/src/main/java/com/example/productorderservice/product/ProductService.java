package com.example.productorderservice.product;

/**
 * 04. ProductService 메서드 구현
 */
class ProductService {
    private final ProductPort productPort;

    ProductService(final ProductPort productPort) {
        this.productPort = productPort;
    }

    public void addProduct(final AddProductRequest request) {
        final Product product = new Product(request.name(), request.price(), request.discountPolicy()); // 05. Product 클래스 생성

        productPort.save(product);
    }
}
