package example.concurrency.database.optimistic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);
                break;
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage());
                break;
            } catch (Exception e) {
                log.error("[" + Thread.currentThread().getName() + "] fail to update stock");
                Thread.sleep(50);
            }
        }
    }
}