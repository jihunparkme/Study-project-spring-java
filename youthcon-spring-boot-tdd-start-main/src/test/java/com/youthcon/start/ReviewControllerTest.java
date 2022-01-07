package com.youthcon.start;

import com.youthcon.start.application.ReviewService;
import com.youthcon.start.domain.Review;
import com.youthcon.start.error.ReviewNotFoundException;
import com.youthcon.start.ui.ReviewController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @AutoConfigureMockMvc, @SpringBootTest 를 사용할 수 있지만 테스트 환경이 너무 무거워지는 단점.
 * @WebMvcTest 는 Controller 에 있는 Bean 들만 로드 (특정 컨트롤러를 지정할 수 있음)
 */
@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * @MockBean 으로  reviewService 를 주입받았다고 가정할 수 있음
     */
    @MockBean
    private ReviewService reviewService;

    private Long id = 1L;
    private String content = "재밌어요";
    private String phoneNumber = "010-1111-2222";

    @Test
    void 후기_조회_성공() throws Exception {
        //given
        given(reviewService.getById(id))
                .willReturn(new Review(id, content, phoneNumber));

        //when
        ResultActions perform = mockMvc.perform(get("/reviews/" + id));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("content").value(content))
                .andExpect(jsonPath("phoneNumber").value(phoneNumber));
    }

    @Test
    void 후기_조회_실패() throws Exception {
        //given
        given(reviewService.getById(1000L))
                .willThrow(new ReviewNotFoundException("no review id : " + 1000));

        //when
        ResultActions perform = mockMvc.perform(get("/reviews/" + 1000));

        //then
        perform
                .andExpect(status().isNotFound());
    }

    @Test
    void 선물하기_성공() throws Exception {
        //given
        given(reviewService.sendGift(id)).willReturn(new Review(id, content, phoneNumber, true));

        //when
        ResultActions perform = mockMvc.perform(put("/reviews/" + id));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("sent").value(true));
    }
}
