package example.concurrency.redis.redisson.service;

import example.concurrency.redis.lettuce.domain.RedisLettuceLockStock;
import example.concurrency.redis.redisson.domain.RedissonLockStock;
import example.concurrency.redis.redisson.domain.RedissonLockStockRepository;
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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class RedissonLockStockFacadeTest {

    @Autowired
    private RedissonLockStockFacade stockService;

    @Autowired
    private RedissonLockStockRepository stockRepository;

    @BeforeEach
    public void before() {
        stockRepository.saveAndFlush(new RedissonLockStock(1L, 1000L));
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("[Redis Redisson Lock]")
    void test() throws InterruptedException {
        final AtomicInteger successCounter = new AtomicInteger(0);
        final AtomicInteger failCounter = new AtomicInteger(0);
        int threadCount = 1500;
        CountDownLatch latch;
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        stockService.decrease(1L, 1L);
                        int existingValue = successCounter.get();
                        successCounter.set(existingValue + 1);
                    } catch (RuntimeException e) {
                        log.error("Stock Exception. {}", e.getMessage());
                        int existingValue = failCounter.get();
                        failCounter.set(existingValue + 1);
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        latch.await();

        RedissonLockStock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0, stock.getQuantity());
        assertEquals(1000, successCounter.get());
        assertEquals(500, failCounter.get());
    }
}