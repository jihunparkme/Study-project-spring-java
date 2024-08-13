package example.concurrency.java.async.service;

import example.concurrency.java.domain.JavaStock;
import example.concurrency.java.domain.JavaStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JavaAsyncStockService {

    private final JavaStockRepository stockRepository;

    public void decrease(Long id, Long quantity) {
        final JavaStock javaStock = stockRepository.findById(id).orElseThrow();
        final Long beforeQuantity = javaStock.getQuantity();
        javaStock.decrease(quantity);

        final JavaStock savedStock = stockRepository.saveAndFlush(javaStock);
        log.info("[quantity] before: {}, after: {}", beforeQuantity, savedStock.getQuantity());
    }
}
