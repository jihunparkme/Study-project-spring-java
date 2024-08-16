package example.concurrency.redis.redisson.service;

import example.concurrency.redis.redisson.domain.RedissonLockStock;
import example.concurrency.redis.redisson.domain.RedissonLockStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedissonLockStockService {

    private final RedissonLockStockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        final RedissonLockStock stock = stockRepository.findById(id).orElseThrow();
        final Long beforeQuantity = stock.getQuantity();
        stock.decrease(quantity);

        final RedissonLockStock savedStock = stockRepository.save(stock);
        log.info("[quantity] before: {}, after: {}", beforeQuantity, savedStock.getQuantity());
    }
}
