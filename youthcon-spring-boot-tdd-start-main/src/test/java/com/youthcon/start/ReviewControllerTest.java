package com.youthcon.start;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 후기_조회_성공() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(get("/reviews/1"));
        //then
        perform.andExpect(status().isOk());
    }

}
