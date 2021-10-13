package az.code.EventsApi.services.interfaces;

import az.code.EventsApi.dto.EventDto;
import az.code.EventsApi.dto.FindEventByDateDto;
import az.code.EventsApi.dto.ReturnEventDto;
import az.code.EventsApi.models.Administrator;
import az.code.EventsApi.models.AppUser;
import az.code.EventsApi.models.Event;

import java.util.List;

public interface EventService {

    Administrator addAdministrator(Administrator administrator);

    AppUser addAppUser(AppUser appUser);

    AppUser getAppUserFromToken();

    Event addEvent(EventDto event, AppUser user);

    Event getEvent(Long id, AppUser user);

    List<ReturnEventDto> getEvents(FindEventByDateDto date);

    Event deleteEventById(Long id, AppUser user);

    Event changeEvent(Long id, AppUser user, EventDto event);

    void checkDate();
}
