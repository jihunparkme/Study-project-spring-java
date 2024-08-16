package example.concurrency.redis.lettuce.controller;

import example.concurrency.redis.lettuce.service.RedisLettuceLockStockFacade;
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
public class RedisLettuceLockStockController {

    private final RedisLettuceLockStockFacade stockService;

    @GetMapping(value = "/buy/{id}/redis/lettuce")
    public ResponseEntity decrease(@PathVariable("id") Long id) {
        try {
            stockService.decrease(id, 1L);
            return ResponseEntity.ok("SUCCESS");
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
