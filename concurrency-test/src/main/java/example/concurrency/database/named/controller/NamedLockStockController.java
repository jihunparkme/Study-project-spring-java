package example.concurrency.database.named.controller;

import example.concurrency.database.named.service.NamedLockStockFacade;
import example.concurrency.database.optimistic.service.OptimisticLockStockFacade;
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
public class NamedLockStockController {

    private final NamedLockStockFacade stockService;

    @GetMapping(value = "/buy/{id}/database/named")
    public ResponseEntity decrease(@PathVariable("id") Long id) {
        try {
            stockService.decrease(id, 1L);
            return ResponseEntity.ok("SUCCESS");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
