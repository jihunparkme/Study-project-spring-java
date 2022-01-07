package com.youthcon.start;

import org.springframework.stereotype.Component;

@Component
public class Delay {
    public Delay() throws InterruptedException {
        Thread.sleep(5000);
    }
}
