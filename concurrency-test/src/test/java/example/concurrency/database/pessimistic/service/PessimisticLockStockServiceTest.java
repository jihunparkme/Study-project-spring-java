package example.concurrency.database.pessimistic.service;

import example.concurrency.database.domain.DatabaseStock;
import example.concurrency.database.domain.DatabaseStockRepository;
import example.concurrency.java.async.service.JavaAsyncStockService;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PessimisticLockStockServiceTest {

    @Autowired
    private PessimisticLockStockService stockService;

    @Autowired
    private DatabaseStockRepository stockRepository;

    @BeforeEach
    void beforeEach() {
        stockRepository.saveAndFlush(new DatabaseStock(1L, 100L));
    }

    @AfterEach
    void afterEach() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("[Pessimistic Lock]")
    void test() throws Exception {
        int threadCount = 1000;
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

        final DatabaseStock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(0, stock.getQuantity());
    }
}