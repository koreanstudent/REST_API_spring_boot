package com.studey.restapi.events;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value ="/api/events", produces = MediaTypes.HAL_JSON_VALUE) //  class 안에 있는 모든 핸들러 들은 MediaTypes.HAL_JSON_VALUE 응답을 보낸다
public class EventController {


    private final EventRepository eventRepository;


    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {

        Event newEvent = this.eventRepository.save(event);


        // HATEOS가 제공하는 linkTo, methodOn 사용
        URI createdUrl = linkTo(EventController.class).slash(newEvent.getId()).toUri(); // location 헤더에 담김
        event.setId(10);

        return ResponseEntity.created(createdUrl).body(event);

    }



}
