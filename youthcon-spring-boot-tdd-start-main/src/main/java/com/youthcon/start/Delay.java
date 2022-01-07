package com.youthcon.start;

import org.springframework.stereotype.Component;

/**
 * 통합 테스트 진행 시 모든 Bean 들을 로드하므로 테스트 환경에서 시간이 오래 걸리는 것을 테스트
 */
@Component
public class Delay {
    public Delay() throws InterruptedException {
        Thread.sleep(5000);
    }
}
