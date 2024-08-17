package stock.kafka.consumer;

import stock.kafka.domain.FailedEvent;
import stock.kafka.domain.FailedEventRepository;
import stock.kafka.domain.KafkaStock;
import stock.kafka.domain.KafkaStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockDecreaseConsumer {

    private final KafkaStockRepository stockRepository;
    private final FailedEventRepository failedEventRepository;

    @KafkaListener(topics = "stock_decrease", groupId = "stock_group")
    public void decrease(String message) {
        final String[] splitMessage = message.split(":");
        final long userId = Long.parseLong(splitMessage[0]);
        final long stockId = Long.parseLong(splitMessage[1]);

        try {
            log.info("[" + Thread.currentThread().getName() + "] [stock_decrease] userId: {}, stockId: {}", userId, stockId);
            final KafkaStock stock = stockRepository.findById(stockId).orElseThrow();
            final Long beforeQuantity = stock.getQuantity();
            stock.decrease(1L);

            final KafkaStock savedStock = stockRepository.saveAndFlush(stock);
            log.info("[quantity] before: {}, after: {}", beforeQuantity, savedStock.getQuantity());
        } catch (Exception e) {
            log.error("failed to decrease stock::{}", stockId, e);
            failedEventRepository.save(new FailedEvent(userId, stockId, e.getMessage()));
        }
    }
}