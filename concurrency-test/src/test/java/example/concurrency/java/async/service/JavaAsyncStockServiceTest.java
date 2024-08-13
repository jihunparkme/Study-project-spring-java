package example.concurrency.java.async.service;

import example.concurrency.java.domain.JavaStock;
import example.concurrency.java.domain.JavaStockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class JavaAsyncStockServiceTest {

    @Autowired
    private JavaAsyncStockService stockService;

    @Autowired
    private JavaStockRepository stockRepository;

    @BeforeEach
    void beforeEach() {
        stockRepository.saveAndFlush(new JavaStock(1L, 100L));
    }

    @AfterEach
    void afterEach() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("[JAVA 비동기] 0의 결과를 예상하지만 실제 갱신이 누락")
    void test() throws Exception {
        int threadCount = 100;
        CountDownLatch latch;
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        stockService.decrease(1L, 1L);
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        latch.await();

        JavaStock stock = stockRepository.findById(1L).orElseThrow();

        assertNotEquals(0, stock.getQuantity());
    }
}