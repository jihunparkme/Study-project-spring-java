package example.concurrency.database.optimistic.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class OptimisticLockStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Long quantity;

    @Version
    private Long version;

    public OptimisticLockStock(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void decrease(Long quantity) {
        if (this.quantity - quantity < 0) {
            throw new IllegalArgumentException("재고는 0보다 작을 수 없습니다.");
        }

        this.quantity -= quantity;
    }
}
