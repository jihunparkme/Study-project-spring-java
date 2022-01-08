package com.youthcon21.handsonlab.springevent.sender.application;

import com.youthcon21.handsonlab.springevent.user.event.UserSenderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SenderEventListener implements ApplicationListener<UserSenderEvent> {

     @Override
    public void onApplicationEvent(UserSenderEvent event) {
         log.info("환영 이메일 발송 성공 : {}", event.getEmail());
         log.info("환영 SMS 발송 성공 : {}", event.getPhoneNumber());
    }
}