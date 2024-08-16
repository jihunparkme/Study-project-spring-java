package example.concurrency.redis.redisson.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RedissonLockStockRepository extends JpaRepository<RedissonLockStock, Long> {
}
