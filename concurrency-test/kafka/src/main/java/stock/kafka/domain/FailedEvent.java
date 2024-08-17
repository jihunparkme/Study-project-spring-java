package stock.kafka.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class FailedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long stockId;

    private String message;

    private LocalDateTime timestamp;

    public FailedEvent(final Long userId, final Long stockId, final String message) {
        this.userId = userId;
        this.stockId = stockId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
