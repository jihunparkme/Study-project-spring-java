package example.concurrency.java.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JavaStockRepository extends JpaRepository<JavaStock, Long> {
}
