package com.youthcon.start;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * mocking 을 사용하여 데이터베이스에 직접 접근하지 않고 비즈니스 영역에만 집중
 * (Service 에서는 Spring 통합 테스트가 아닌 단위 테스트로 진행하는 것이 좋음)
 */
class ReviewServiceTest {

    ReviewRepository reviewRepository = mock(ReviewRepository.class);
    ReviewService reviewService = new ReviewService(reviewRepository);

    // test moking 시 테스트 데이터를 미리 작성해 놓는 것이 편리
    private Long id = 1L;
    private String content = "재밌어요";
    private String phoneNumber = "010-1111-2222";

    @Test
    void 후기_조회_성공() {
        //given
        given(reviewRepository.findById(id))
                .willReturn(Optional.of(new Review(id, content, phoneNumber)));

        //when
        Review review = reviewService.getById(id);

        //then
        assertThat(review.getId()).isEqualTo(id);
        assertThat(review.getContent()).isEqualTo(content);
        assertThat(review.getPhoneNumber()).isEqualTo(phoneNumber);
    }
}