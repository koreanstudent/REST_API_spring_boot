package com.studey.restapi.events;


import org.modelmapper.ModelMapper;


import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value ="/api/events", produces = MediaTypes.HAL_JSON_VALUE) //  class 안에 있는 모든 핸들러 들은 MediaTypes.HAL_JSON_VALUE 응답을 보낸다
public class EventController {


    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;


    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {

        if(errors.hasErrors()){  //Valid 결과
            return ResponseEntity.badRequest().build();
        }

        eventValidator.validate(eventDto,errors);

        if(errors.hasErrors()){  //Valid 결과
            return ResponseEntity.badRequest().build();
        }

        Event event = modelMapper.map(eventDto,Event.class);

        Event newEvent = this.eventRepository.save(event);


        // HATEOS가 제공하는 linkTo, methodOn 사용
        URI createdUrl = linkTo(EventController.class).slash(newEvent.getId()).toUri(); // location 헤더에 담김

        return ResponseEntity.created(createdUrl).body(event);

    }



}
