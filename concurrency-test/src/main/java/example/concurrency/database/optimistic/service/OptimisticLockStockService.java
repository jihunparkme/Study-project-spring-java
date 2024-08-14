package example.concurrency.database.optimistic.service;

import example.concurrency.database.optimistic.domain.OptimisticLockRepository;
import example.concurrency.database.optimistic.domain.OptimisticLockStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptimisticLockStockService {

    private final OptimisticLockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        final OptimisticLockStock stock = stockRepository.findByIdWithOptimisticLock(id);
        final Long beforeQuantity = stock.getQuantity();
        stock.decrease(quantity);

        final OptimisticLockStock savedStock = stockRepository.save(stock);
        log.info("[" + Thread.currentThread().getName() + "] [quantity] before: {}, after: {}", beforeQuantity, savedStock.getQuantity());
    }
}
