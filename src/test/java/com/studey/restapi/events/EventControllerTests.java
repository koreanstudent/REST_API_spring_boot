package com.studey.restapi.events;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTests {


    @Autowired
    MockMvc mockMvc;  // 웹서버 없이 가짜 요청을 dispatherservlet 보내 테스트 -> 컨트롤러 테스트용으로 자주 쓰임

    @Test
    public void createEvent() throws Exception{
        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)  // Hypertext Application Language
                
        )
                .andExpect(status().isCreated()); // 201
    }


}
