package example.concurrency.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadUtil {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("인터럽트 발생. {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
