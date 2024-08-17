package example.concurrency.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockDecreaseProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void create(final Long userId, final Long stockId) {
        kafkaTemplate.send("stock_decrease", userId + ":" + stockId);
    }
}
