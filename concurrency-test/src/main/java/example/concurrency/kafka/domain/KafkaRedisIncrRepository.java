package example.concurrency.kafka.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KafkaRedisIncrRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Long increment(final Long stockId) {
        return redisTemplate
                .opsForValue()
                .increment(Long.toString(stockId));
    }

    public Long getCount(final Long id) {
        final String result = redisTemplate
                .opsForValue()
                .get(Long.toString(id));
        return Long.parseLong(result == null ? "0" : result);
    }
}
