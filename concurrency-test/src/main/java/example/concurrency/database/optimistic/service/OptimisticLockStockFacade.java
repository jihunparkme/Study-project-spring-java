package example.concurrency.database.optimistic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import static example.concurrency.common.util.ThreadUtil.sleep;

@Slf4j
@Component
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    public void decrease(Long id, Long quantity) {
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);
                break;
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e);
            } catch (ObjectOptimisticLockingFailureException e) {
                log.error("[" + Thread.currentThread().getName() + "] fail to update stock");
                sleep(100);
            }
        }
    }
}