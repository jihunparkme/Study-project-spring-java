package example.concurrency.common.controller;

import example.concurrency.database.optimistic.domain.OptimisticLockRepository;
import example.concurrency.database.optimistic.domain.OptimisticLockStock;
import example.concurrency.database.pessimistic.domain.PessimisticLockRepository;
import example.concurrency.database.pessimistic.domain.PessimisticLockStock;
import example.concurrency.java.domain.JavaStock;
import example.concurrency.java.domain.JavaStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/stock")
public class StockController {
    private final JavaStockRepository stockRepository;
    private final PessimisticLockRepository pessimisticLockRepository;
    private final OptimisticLockRepository optimisticLockRepository;

    @GetMapping(value = "/save/java/{id}/{quantity}")
    public JavaStock saveJavaStock(@PathVariable("id") Long id, @PathVariable("quantity") Long quantity) {
        return stockRepository.saveAndFlush(new JavaStock(id, quantity));
    }

    @GetMapping(value = "/save/database/pessimistic/{id}/{quantity}")
    public PessimisticLockStock savePessimisticLockStock(@PathVariable("id") Long id, @PathVariable("quantity") Long quantity) {
        return pessimisticLockRepository.saveAndFlush(new PessimisticLockStock(id, quantity));
    }
}
