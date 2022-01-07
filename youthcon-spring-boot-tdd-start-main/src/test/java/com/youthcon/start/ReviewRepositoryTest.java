package com.youthcon.start;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void 후기_조회_성공() {
        //given
        //when
        Review review = reviewRepository.findById(1L)
                                      .orElseThrow(RuntimeException::new);

        //then
        assertThat(review.getId()).isEqualTo(1L);
    }
}