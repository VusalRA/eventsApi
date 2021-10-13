package az.code.EventsApi.dto;

import az.code.EventsApi.enums.ColorTitleBar;
import az.code.EventsApi.models.AppUser;
import az.code.EventsApi.models.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReturnEventDto {

    private String title;
    private Boolean readStatus;
    private ColorTitleBar colorTitleBar;

    public ReturnEventDto(Event event, AppUser user) {
        this.title = event.getTitle();
        if (event.getReadList().contains(user)) {
            readStatus = true;
        } else {
            readStatus = false;
        }
        this.colorTitleBar = event.getColorTitleBar();
    }
}
