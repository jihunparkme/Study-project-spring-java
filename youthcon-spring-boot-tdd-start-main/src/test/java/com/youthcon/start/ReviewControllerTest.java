package com.youthcon.start;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ReviewService reviewService;

    @Test
    void 후기_조회_성공() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(get("/reviews/1"));
        //then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("content").value("재밌어요"))
                .andExpect(jsonPath("phoneNumber").value("010-1111-2222"));
    }

}
