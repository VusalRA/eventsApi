package az.code.EventsApi.models;

import az.code.EventsApi.enums.EventType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private EventType type;
    private String description;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
