package example.concurrency.database.named.service;

import example.concurrency.database.named.domain.NamedLockRepository;
import example.concurrency.database.named.domain.NamedLockStock;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class NamedLockStockServiceTest {

    @Autowired
    private NamedLockStockFacade stockService;

    @Autowired
    private NamedLockRepository stockRepository;

    @BeforeEach
    void beforeEach() {
        stockRepository.saveAndFlush(new NamedLockStock(1L, 100L));
    }

    @AfterEach
    void afterEach() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("[Named Lock]")
    void test() throws Exception {
        int threadCount = 100;
        CountDownLatch latch;
        try (ExecutorService executorService = Executors.newFixedThreadPool(32)) {
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

        final NamedLockStock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(0, stock.getQuantity());
    }
}