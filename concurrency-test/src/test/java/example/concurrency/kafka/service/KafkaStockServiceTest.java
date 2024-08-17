package example.concurrency.kafka.service;

import example.concurrency.kafka.domain.KafkaRedisIncrRepository;
import example.concurrency.kafka.domain.KafkaStock;
import example.concurrency.kafka.domain.KafkaStockRepository;
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

import static jodd.util.ThreadUtil.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class KafkaStockServiceTest {

    @Autowired
    private KafkaStockService stockService;
    @Autowired
    private KafkaStockRepository stockRepository;
    @Autowired
    private KafkaRedisIncrRepository redisIncrRepository;


    @BeforeEach
    void beforeEach() {
        stockRepository.saveAndFlush(new KafkaStock(1L, 1000L));
    }

    @AfterEach
    void afterEach() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("[Kafka + Redis Lock]")
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
                        stockService.decrease(123L, 1L);
                        successCounter.incrementAndGet();
                    } catch (IllegalArgumentException e) {
                        log.error("Stock Exception. {}", e.getMessage());
                        failCounter.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        latch.await();

        sleep(10000);

        final Long count = redisIncrRepository.getCount(1L);
        assertEquals(2000, count);
        assertEquals(1000, successCounter.get());
        assertEquals(1000, failCounter.get());
    }
}