package com.studey.restapi.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.studey.restapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {


    @Autowired
    MockMvc mockMvc;  // 웹서버 없이 가짜 요청을 dispatherservlet 보내 테스트 -> 컨트롤러 테스트용으로 자주 쓰임

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean
//    EventRepository eventRepository;


    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")  // 테스트 셜명
    public void createEvent() throws Exception{
        EventDto event = EventDto.builder()
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

//        event.setId(10);
//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)// Hypertext Application Language
                .content(objectMapper.writeValueAsString(event)))  // 객체를 JSON으로 변환
                .andDo(print())
                .andExpect(status().isCreated()) // 201
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))

        ;

    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception{
        Event event = Event.builder()
                .id(100)
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
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();


        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)// Hypertext Application Language
                .content(objectMapper.writeValueAsString(event)))  // 객체를 JSON으로 변환
                .andDo(print())
                .andExpect(status().isBadRequest())


        ;

    }

    @Test
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("spring")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,11,21,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,10,21,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,9,21,21))
                .endEventDateTime(LocalDateTime.of(2018,11,8,21,21))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists());
    }

}
