package com.youthcon21.handsonlab.springevent.user.application;

import com.youthcon21.handsonlab.springevent.user.domain.User;
import com.youthcon21.handsonlab.springevent.user.event.UserAdminEvent;
import com.youthcon21.handsonlab.springevent.user.event.UserCouponEvent;
import com.youthcon21.handsonlab.springevent.user.event.UserSenderEvent;
import com.youthcon21.handsonlab.springevent.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    /**
     * 실행되는 이벤트 정보를 저장
     */
    @Captor
    private ArgumentCaptor<Object> eventPublisherCaptor;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, eventPublisher);
    }

    @Test
    @DisplayName("이벤트 발행 테스트")
    void eventTest() {
        given(userRepository.save(any(User.class)))
                .willReturn(new User("jihunpark", "jihunpark.tech@gmail.com", "010-1234-1234"));

        UserRequest request = new UserRequest("jihunpark", "jihunpark.tech@gmail.com", "010-1234-1234");
        userService.create(request);

        verify(eventPublisher, times(3)).publishEvent(eventPublisherCaptor.capture());

        List<Object> allValues = eventPublisherCaptor.getAllValues();

        assertThat(allValues.get(0)).isInstanceOf(UserAdminEvent.class);
        assertThat(allValues.get(1)).isInstanceOf(UserCouponEvent.class);
        assertThat(allValues.get(2)).isInstanceOf(UserSenderEvent.class);
    }
}