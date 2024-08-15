package example.concurrency.redis.incr.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RedisIncrStockRepository extends JpaRepository<RedisIncrStock, Long> {
}
