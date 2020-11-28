package com.studey.restapi.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTests {


    @Autowired
    MockMvc mockMvc;  // 웹서버 없이 가짜 요청을 dispatherservlet 보내 테스트 -> 컨트롤러 테스트용으로 자주 쓰임

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;


    @Test
    public void createEvent() throws Exception{
        Event event = Event.builder()
                .name("spring")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,11,21,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,21,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,21,21))
                .endEventDateTime(LocalDateTime.of(2018,11,29,21,21))
                .basePrice(200)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)// Hypertext Application Language
                .content(objectMapper.writeValueAsString(event)))  // 객체를 JSON으로 변환
                .andDo(print())
                .andExpect(status().isCreated()) // 201
                .andExpect(jsonPath("id").exists());

    }


}
