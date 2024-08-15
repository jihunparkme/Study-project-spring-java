package example.concurrency.redis.lettuce.service;

import example.concurrency.redis.lettuce.domain.RedisLettuceLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static example.concurrency.common.util.ThreadUtil.sleep;

@Component
@RequiredArgsConstructor
public class RedisLettuceLockStockFacade {

    private final RedisLettuceLockRepository redisLockRepository;
    private final RedisLettuceLockStockService stockService;

    public void decrease(Long key, Long quantity) {
        while (!redisLockRepository.lock(key)) {
            sleep(100);
        }

        try {
            stockService.decrease(key, quantity);
        } finally {
            redisLockRepository.unlock(key);
        }
    }
}