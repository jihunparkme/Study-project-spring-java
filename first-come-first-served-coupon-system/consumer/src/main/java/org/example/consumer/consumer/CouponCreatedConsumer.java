package org.example.consumer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.consumer.domain.Coupon;
import org.example.consumer.domain.FailedEvent;
import org.example.consumer.repository.CouponRepository;
import org.example.consumer.repository.FailedEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;
    private final FailedEventRepository failedEventRepository;

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId) {
        try {
            couponRepository.save(new Coupon(userId));
        } catch (Exception e) {
            log.error("failed to crate coupon::{}", userId, e);
            failedEventRepository.save(new FailedEvent(userId));
        }
    }
}
