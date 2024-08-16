package example.concurrency.redis.lettuce.service;

import example.concurrency.redis.lettuce.domain.RedisLettuceLockStock;
import example.concurrency.redis.lettuce.domain.RedisLettuceLockStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisLettuceLockStockService {

    private final RedisLettuceLockStockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        final RedisLettuceLockStock stock = stockRepository.findById(id).orElseThrow();
        final Long beforeQuantity = stock.getQuantity();
        stock.decrease(quantity);

        final RedisLettuceLockStock savedStock = stockRepository.save(stock);
        log.info("[quantity] before: {}, after: {}", beforeQuantity, savedStock.getQuantity());
    }
}
