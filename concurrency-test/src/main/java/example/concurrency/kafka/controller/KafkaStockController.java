package example.concurrency.kafka.controller;

import example.concurrency.kafka.service.KafkaStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/stock")
public class KafkaStockController {
    private final KafkaStockService stockService;

    @GetMapping(value = "/buy/{userId}/{stockId}/kafka")
    public ResponseEntity decrease(@PathVariable("userId") Long userId, @PathVariable("stockId") Long stockId) {
        try {
            stockService.decrease(userId, stockId);
            return ResponseEntity.ok("SUCCESS");
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
