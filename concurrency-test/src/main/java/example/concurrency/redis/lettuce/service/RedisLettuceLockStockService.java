package example.concurrency.redis.lettuce.service;

import example.concurrency.redis.lettuce.domain.RedisLettuceLockStock;
import example.concurrency.redis.lettuce.domain.RedisLettuceLockStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RedisLettuceLockStockService {

    private final RedisLettuceLockStockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        final RedisLettuceLockStock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
