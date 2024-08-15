package example.concurrency.redis.lettuce.service;

import example.concurrency.redis.lettuce.domain.RedisLettuceLockStock;
import example.concurrency.redis.lettuce.domain.RedisLettuceLockStockRepository;
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
class RedisLettuceLockStockFacadeTest {

    @Autowired
    private RedisLettuceLockStockFacade stockService;

    @Autowired
    private RedisLettuceLockStockRepository stockRepository;


    @BeforeEach
    void beforeEach() {
        stockRepository.saveAndFlush(new RedisLettuceLockStock(1L, 1000L));
    }

    @AfterEach
    void afterEach() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("[Redis Lettuce Lock]")
    void test() throws Exception {
        int threadCount = 1000;
        CountDownLatch latch;
        try (ExecutorService executorService = Executors.newFixedThreadPool(32)) {
            latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        stockService.decrease(1L, 1L);
                    } catch (IllegalArgumentException e) {
                        log.error("Stock Exception. {}", e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        latch.await();

        RedisLettuceLockStock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0, stock.getQuantity());
    }
}