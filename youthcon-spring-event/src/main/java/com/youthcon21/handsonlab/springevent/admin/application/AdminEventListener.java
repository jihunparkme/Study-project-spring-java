package com.youthcon21.handsonlab.springevent.admin.application;

import com.youthcon21.handsonlab.springevent.user.event.UserAdminEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdminEventListener implements ApplicationListener<UserAdminEvent> {

    @Override
    public void onApplicationEvent(UserAdminEvent event) {
        log.info("어드민 서비스 : {}님이 가입했습니다", event.getUsername());
    }
}
