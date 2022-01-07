package com.youthcon.start;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewServiceTest {

    ReviewRepository reviewRepository;
    ReviewService reviewService = new ReviewService();

    @Test
    void 후기_조회_성공() {
        //given

        //when
        Review review = reviewService.getById(1L);

        //then
        assertThat(review.getId()).isEqualTo(1);
    }
}