package example.concurrency.redis.incr.service;

import example.concurrency.redis.incr.domain.RedisIncrRepository;
import example.concurrency.redis.incr.domain.RedisIncrStock;
import example.concurrency.redis.incr.domain.RedisIncrStockRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class RedisIncrStockServiceTest {

    @Autowired
    private RedisIncrStockService stockService;

    @Autowired
    private RedisIncrStockRepository stockRepository;

    @Autowired
    private RedisIncrRepository redisStockRepository;

    @BeforeEach
    void beforeEach() {
        stockRepository.saveAndFlush(new RedisIncrStock(1L, 1000L));
    }

    @AfterEach
    void afterEach() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("[Redis Incr]")
    void test() throws Exception {
        final AtomicInteger successCounter = new AtomicInteger(0);
        final AtomicInteger failCounter = new AtomicInteger(0);
        int threadCount = 2000;
        CountDownLatch latch;
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        stockService.decrease(1L);
                        int existingValue = successCounter.get();
                        successCounter.set(existingValue + 1);
                    } catch (IllegalArgumentException e) {
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

        final Long count = redisStockRepository.getCount(1L);
        assertEquals(2000, count);
        assertEquals(1000, successCounter.get());
        assertEquals(1000, failCounter.get());
    }
}