package example.concurrency.kafka.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class KafkaStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Long quantity;

    public KafkaStock(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void validQuantity(Long quantity) {
        if (quantity > this.quantity) {
            throw new IllegalArgumentException("재고는 0보다 작을 수 없습니다.");
        }
    }
}
