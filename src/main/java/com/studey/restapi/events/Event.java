package com.studey.restapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;



@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id")  //
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING) // Enum 순서가 꼬일수도있어 string으로 권장
    private EventStatus eventStatus = EventStatus.DRAFT;

    public void update() {
        if(this.basePrice == 0 && this.maxPrice == 0){
            this.free =true;
        }else {
            this.free = false;
        }

        if("".equals(this.location) || this.location == null){  // 자바 11은 isblank
            this.offline =false;
        }else {
            this.offline = true;
        }


    }
}
