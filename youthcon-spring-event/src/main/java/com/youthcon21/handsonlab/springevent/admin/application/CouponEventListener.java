package com.youthcon21.handsonlab.springevent.admin.application;

import com.youthcon21.handsonlab.springevent.user.event.UserCouponEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CouponEventListener implements ApplicationListener<UserCouponEvent> {

    @Override
    public void onApplicationEvent(UserCouponEvent event) {
        log.info("쿠폰 등록 완료 : {}", event.getEmail());
    }
}