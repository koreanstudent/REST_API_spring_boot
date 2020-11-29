package com.studey.restapi.events;


import org.modelmapper.ModelMapper;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto,errors);

        if(errors.hasErrors()){  //Valid 결과
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto,Event.class);
        event.update();

        Event newEvent = this.eventRepository.save(event);


        // HATEOS가 제공하는 linkTo, methodOn 사용
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUrl = selfLinkBuilder.toUri(); // location 헤더에 담김

        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
//        eventResource.add(selfLinkBuilder.withSelfRel());  // eventresource에 추가
        eventResource.add(selfLinkBuilder.withRel("update-event"));

        eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));


        return ResponseEntity.created(createdUrl).body(eventResource);

    }



}
