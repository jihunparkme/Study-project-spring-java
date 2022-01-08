package com.youthcon21.handsonlab.springevent.user.application;

import com.youthcon21.handsonlab.springevent.user.domain.User;
import com.youthcon21.handsonlab.springevent.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * ApplicationEventPublisher 를 활용하여 Service 의존성을 줄일 수 있다.
     * 단, 이벤트 Class, Listener 를 생성해주어야 하는 단점이 발생한다.
     * @param userRepository
     * @param eventPublisher
     */
    public UserService(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public void create(UserRequest userRequest) {
        User user = new User(
                userRequest.getName(),
                userRequest.getEmail(),
                userRequest.getPhoneNumber()
        );
        userRepository.save(user);

        user.registerEventPublish(eventPublisher);
    }

    public UserResponse get(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(""));

        return new UserResponse(
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
}