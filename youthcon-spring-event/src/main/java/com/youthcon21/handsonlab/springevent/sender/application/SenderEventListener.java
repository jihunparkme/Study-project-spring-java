package com.youthcon21.handsonlab.springevent.sender.application;

import com.youthcon21.handsonlab.springevent.user.event.UserSenderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SenderEventListener {

    @EventListener
    public void handleEmail(UserSenderEvent event) {
         log.info("환영 이메일 발송 성공 : {}", event.getEmail());
    }

    @EventListener
    public void handleSMS(UserSenderEvent event) {
        log.info("환영 SMS 발송 성공 : {}", event.getPhoneNumber());
    }
}