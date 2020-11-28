package com.studey.restapi.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("test")
                .description("rest api development with spring")
                .build();

        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        // Given
        String name = "event";
        String spring = "spring";


        // when
        Event event = new Event();
        event.setName("event");
        event.setDescription("spring");

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(spring);

    }


}