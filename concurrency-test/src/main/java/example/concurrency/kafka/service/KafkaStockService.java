package example.concurrency.kafka.service;

import example.concurrency.kafka.domain.KafkaRedisIncrRepository;
import example.concurrency.kafka.domain.KafkaStock;
import example.concurrency.kafka.domain.KafkaStockRepository;
import example.concurrency.kafka.producer.StockDecreaseProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaStockService {

    private final KafkaStockRepository stockRepository;
    private final StockDecreaseProducer stockDecreaseProducer;
    private final KafkaRedisIncrRepository redisIncrRepository;

    public void decrease(final Long userId, final Long stockId) {
        final KafkaStock stock = stockRepository.findById(stockId).orElseThrow();
        final Long count = redisIncrRepository.increment(stockId);
        stock.validQuantity(count);

        stockDecreaseProducer.create(userId, stockId);
        log.info("[" + Thread.currentThread().getName() + "] [quantity] incr count: {}", count);
    }
}
