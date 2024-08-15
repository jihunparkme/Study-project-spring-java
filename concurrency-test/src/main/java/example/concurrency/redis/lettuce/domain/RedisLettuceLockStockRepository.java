package example.concurrency.redis.lettuce.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RedisLettuceLockStockRepository extends JpaRepository<RedisLettuceLockStock, Long> {
}
