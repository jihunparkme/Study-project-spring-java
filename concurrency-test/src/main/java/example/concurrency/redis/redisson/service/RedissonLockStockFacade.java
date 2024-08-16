package example.concurrency.redis.redisson.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;

    private final RedissonLockStockService stockService;

    public void decrease(Long key, Long quantity) {
        RLock lock = redissonClient.getLock(key.toString());

        try {
            /**
             * (락 획득 시도 시간, 점유 시간)
             * 20초 동안 락 획득 시도, 획득할 경우 3초 안에 해제 예정
             */
            boolean available = lock.tryLock(20, 3, TimeUnit.SECONDS);
            if (!available) {
                log.error("Redisson Lock 획득 실패");
                return;
            }

            stockService.decrease(key, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
