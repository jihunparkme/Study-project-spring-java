package example.concurrency.database.optimistic.service;

import example.concurrency.database.optimistic.domain.OptimisticLockRepository;
import example.concurrency.database.optimistic.domain.OptimisticLockStock;
import example.concurrency.database.pessimistic.domain.PessimisticLockRepository;
import example.concurrency.database.pessimistic.domain.PessimisticLockStock;
import example.concurrency.database.pessimistic.service.PessimisticLockStockService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@SpringBootTest
class OptimisticLockStockServiceTest {

    @Autowired
    private OptimisticLockStockFacade stockService;

    @Autowired
    private OptimisticLockRepository stockRepository;

    @BeforeEach
    void beforeEach() {
        stockRepository.saveAndFlush(new OptimisticLockStock(1L, 100L));
    }

    @AfterEach
    void afterEach() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("[Optimistic Lock]")
    void test() throws Exception {
        int threadCount = 1000;
        CountDownLatch latch;
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        stockService.decrease(1L, 1L);
                    } catch (IllegalArgumentException e) {
                        log.error("RuntimeException {}", e.getMessage());
                    } catch (InterruptedException e) {
                        log.error("InterruptedException {}", e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        latch.await();

        final OptimisticLockStock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(0, stock.getQuantity());
    }
}