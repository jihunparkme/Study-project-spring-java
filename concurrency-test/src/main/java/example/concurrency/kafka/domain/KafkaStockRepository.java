package example.concurrency.kafka.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaStockRepository extends JpaRepository<KafkaStock, Long> {
}
