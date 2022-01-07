package com.youthcon.start;

import com.youthcon.start.application.ReviewService;
import com.youthcon.start.domain.Review;
import com.youthcon.start.error.DuplicateSendGiftException;
import com.youthcon.start.error.ReviewNotFoundException;
import com.youthcon.start.error.SendGiftInternalException;
import com.youthcon.start.infra.GiftApi;
import com.youthcon.start.infra.ReviewRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * mocking 을 사용하여 데이터베이스에 직접 접근하지 않고 비즈니스 영역에만 집중
 * (Service 에서는 Spring 통합 테스트가 아닌 단위 테스트로 진행하는 것이 좋음)
 */
class ReviewServiceTest {

    private GiftApi giftApi = mock(GiftApi.class);
    private ReviewRepository reviewRepository = mock(ReviewRepository.class);
    private ReviewService reviewService = new ReviewService(giftApi, reviewRepository);

    // test moking 시 테스트 데이터를 미리 작성해 놓는 것이 편리
    private Long id = 1L;
    private String content = "재밌어요";
    private String phoneNumber = "010-1111-2222";

    @Test
    void 후기_조회_성공() {
        //given
        given(reviewRepository.findById(id))
                .willReturn(Optional.of(new Review(id, content, phoneNumber, false)));

        //when
        Review review = reviewService.getById(id);

        //then
        assertThat(review.getId()).isEqualTo(id);
        assertThat(review.getContent()).isEqualTo(content);
        assertThat(review.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void 후기_조회_실패() {
        //given
        given(reviewRepository.findById(1000L))
                .willReturn(Optional.empty());

        assertThatThrownBy(() ->
                //when
                reviewRepository.getById(1000L))
                //then
                .isInstanceOf(ReviewNotFoundException.class);
    }

    @Test
    void 선물하기_성공(){
        // 준비
        given(reviewRepository.findById(id)).willReturn(Optional.of(new Review(id, content, phoneNumber, false)));
        given(giftApi.send(phoneNumber)).willReturn(true);
        given(reviewRepository.save(any(Review.class))).willReturn(new Review(id, content, phoneNumber, true));

        //실행
        Review review = reviewService.sendGift(id);

        // 검증
        assertThat(review.getId()).isEqualTo(id);
        assertThat(review.getSent()).isEqualTo(true);
    }

    @Test
    void 선물하기_중복_지급_실패(){
        //given
        given(reviewRepository.findById(id)).willReturn(Optional.of(new Review(id, content, phoneNumber, true)));

        assertThatThrownBy(() ->
                //when
                reviewService.sendGift(id))
                //then
                .isInstanceOf(DuplicateSendGiftException.class);
    }

    @Test
    void 선물하기_외부_요청_실패(){
        //given
        given(reviewRepository.findById(id)).willReturn(Optional.of(new Review(id, content, phoneNumber, false)));
        given(giftApi.send(phoneNumber)).willReturn(false);

        assertThatThrownBy(() ->
                //when
                reviewService.sendGift(id))
                //then
                .isInstanceOf(SendGiftInternalException.class);
    }
}