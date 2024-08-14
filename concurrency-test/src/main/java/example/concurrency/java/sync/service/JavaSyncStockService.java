package example.concurrency.java.sync.service;

import example.concurrency.java.domain.JavaStock;
import example.concurrency.java.domain.JavaStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JavaSyncStockService {

    private final JavaStockRepository stockRepository;

    @Transactional
    public synchronized void decrease(Long id, Long quantity) {
        final JavaStock javaStock = stockRepository.findById(id).orElseThrow();
        final Long beforeQuantity = javaStock.getQuantity();
        javaStock.decrease(quantity);

        final JavaStock savedStock = stockRepository.saveAndFlush(javaStock);
        log.info("[quantity] before: {}, after: {}", beforeQuantity, savedStock.getQuantity());
    }
}
