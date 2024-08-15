package example.concurrency.database.named.service;

import example.concurrency.database.named.domain.NamedLockRepository;
import example.concurrency.database.named.domain.NamedLockStock;
import example.concurrency.database.pessimistic.domain.PessimisticLockRepository;
import example.concurrency.database.pessimistic.domain.PessimisticLockStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NamedLockStockService {

    private final NamedLockRepository stockRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(Long id, Long quantity) {
        final NamedLockStock stock = stockRepository.findById(id).orElseThrow();
        final Long beforeQuantity = stock.getQuantity();
        stock.decrease(quantity);

        final NamedLockStock savedStock = stockRepository.save(stock);
        log.info("[quantity] before: {}, after: {}", beforeQuantity, savedStock.getQuantity());
    }
}
