package example.concurrency.database.pessimistic.service;

import example.concurrency.database.pessimistic.domain.PessimisticLockStock;
import example.concurrency.database.pessimistic.domain.PessimisticLockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PessimisticLockStockService {

    private final PessimisticLockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        final PessimisticLockStock stock = stockRepository.findByIdWithPessimisticLock(id);
        final Long beforeQuantity = stock.getQuantity();
        stock.decrease(quantity);

        final PessimisticLockStock savedStock = stockRepository.save(stock);
        log.info("[quantity] before: {}, after: {}", beforeQuantity, savedStock.getQuantity());
    }
}
