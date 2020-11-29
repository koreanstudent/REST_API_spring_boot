package com.studey.restapi.events;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() >0){
            errors.rejectValue("basePrice","wrongValue","Baseprice is wrong");
            errors.rejectValue("maxPrice","wrongValue","maxPrice is wrong");

        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();

        if(endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) || endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())
            || endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("endEventDateTime","wrongValue","endEventDateTime is Wrong");
        }

        // TODO beginEventDateTime
        // TODO CloseEnrollmentDateTime

    }
}
