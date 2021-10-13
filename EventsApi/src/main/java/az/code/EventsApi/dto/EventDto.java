package az.code.EventsApi.dto;

import az.code.EventsApi.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventDto {

    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private EventType type;
    private String date;

}
