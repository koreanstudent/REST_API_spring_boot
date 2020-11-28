package com.studey.restapi.events;


import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;


    public EventController(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody EventDto eventDto) {

        Event event = modelMapper.map(eventDto,Event.class);

        Event newEvent = this.eventRepository.save(event);


        // HATEOS가 제공하는 linkTo, methodOn 사용
        URI createdUrl = linkTo(EventController.class).slash(newEvent.getId()).toUri(); // location 헤더에 담김

        return ResponseEntity.created(createdUrl).body(event);

    }



}
