package com.studey.restapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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

    @Test
    @Parameters
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // when
        event.update();

        assertThat(event.isFree()).isEqualTo(isFree);

    }

    private Object[] parametersForTestFree(){
        return new Object[] {
                new Object[] {0, 0, true},
                new Object[] {100, 0, false},
                new Object[] {0, 100, false},
                new Object[] {100, 200, false}
        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline){
        Event event = Event.builder()
                .location(location)
                .build();

        // when
        event.update();

        assertThat(event.isOffline()).isEqualTo(isOffline);

    }

    private Object[] parametersForTestOffline(){
        return new Object[] {
                new Object[] {"강남", true},
                new Object[] {null, false},
                new Object[] {"          ", false},
        };
    }


}