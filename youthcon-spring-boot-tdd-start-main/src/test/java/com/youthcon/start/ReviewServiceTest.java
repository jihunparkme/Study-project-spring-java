package com.youthcon.start;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Test
    void 후기_조회_성공() {
        //given

        //when
        Review review = reviewService.getById(1L);

        //then
        assertThat(review.getId()).isEqualTo(1);
    }
}