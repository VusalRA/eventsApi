package az.code.EventsApi.services.interfaces;

import az.code.EventsApi.dto.EventDto;
import az.code.EventsApi.dto.FindEventByDateDto;
import az.code.EventsApi.dto.ReturnEventDto;
import az.code.EventsApi.models.AppUser;
import az.code.EventsApi.models.Event;
import az.code.EventsApi.models.User;

import java.util.List;

public interface EventService {

    User addUser(User user);

    AppUser addAppUser(AppUser appUser);

    AppUser getAppUserFromToken();

    Event addEvent(EventDto event, AppUser user);

    Event getEvent(Long id, AppUser user);

    List<ReturnEventDto> getEvents(FindEventByDateDto date);

    Event deleteEventById(Long id, AppUser user);

    Event updateEvent(Long id, AppUser user, EventDto event);

    void checkDate();

    List<Event> getReports();
}
