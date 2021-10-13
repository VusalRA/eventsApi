package az.code.EventsApi.models;

import az.code.EventsApi.enums.ColorTitleBar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private String type;
    private String description;
    private LocalDate date;
    private String createdBy;
    private ColorTitleBar colorTitleBar;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<AppUser> readList = new ArrayList<>();
}
