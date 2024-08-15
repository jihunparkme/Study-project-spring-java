package example.concurrency.redis.incr.service;

import example.concurrency.redis.incr.domain.RedisIncrRepository;
import example.concurrency.redis.incr.domain.RedisIncrStock;
import example.concurrency.redis.incr.domain.RedisIncrStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisIncrStockService {

    private final RedisIncrStockRepository stockRepository;
    private final RedisIncrRepository redisStockRepository;

    public void decrease(Long id) {
        final RedisIncrStock stock = stockRepository.findById(id).orElseThrow();
        final Long count = redisStockRepository.increment(id);
        stock.decrease(count);

        log.info("[" + Thread.currentThread().getName() + "] [quantity] incr count: {}", count);
    }
}
