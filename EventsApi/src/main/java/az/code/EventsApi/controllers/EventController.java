package az.code.EventsApi.controllers;

import az.code.EventsApi.dto.EventDto;
import az.code.EventsApi.dto.FindEventByDateDto;
import az.code.EventsApi.dto.ReturnEventDto;
import az.code.EventsApi.dto.UserDto;
import az.code.EventsApi.enums.Role;
import az.code.EventsApi.exceptions.EmailAlreadyTakenException;
import az.code.EventsApi.exceptions.IncorrectNameAndSurnameException;
import az.code.EventsApi.models.AppUser;
import az.code.EventsApi.models.Event;
import az.code.EventsApi.models.User;
import az.code.EventsApi.models.security.LoginUser;
import az.code.EventsApi.security.AuthenticateService;
import az.code.EventsApi.services.interfaces.EventService;
import az.code.EventsApi.utils.RegistrationValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class EventController {

    private RegistrationValidator registrationValidator;
    private AuthenticateService authenticateService;
    private EventService eventService;


    public EventController(RegistrationValidator registrationValidator, AuthenticateService authenticateService, EventService eventService) {
        this.registrationValidator = registrationValidator;
        this.authenticateService = authenticateService;
        this.eventService = eventService;
    }

    @PostMapping("/register")
    public UserDto saveAdministrator(@RequestBody UserDto administrator) {
        if (!registrationValidator.checkNameAndSurname(administrator.getName())) {
            throw new IncorrectNameAndSurnameException();
        }
        if (registrationValidator.checkEmailIsPresent(administrator.getEmail())) {
            throw new EmailAlreadyTakenException();
        }

        User createUser = User.builder().name(administrator.getName()).surname(administrator.getSurname()).build();
        AppUser user = AppUser.builder().email(administrator.getEmail()).password(administrator.getPassword()).role(Role.ADMIN.name()).build();
        eventService.addUser(createUser);
        eventService.addAppUser(user);
        return administrator;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/user")
    public UserDto saveUser(@RequestBody UserDto userDto) {
        if (registrationValidator.checkEmailIsPresent(userDto.getEmail())) {
            throw new EmailAlreadyTakenException();
        }
        User createUser = User.builder().name(userDto.getName()).surname(userDto.getSurname()).build();
        AppUser user = AppUser.builder().email(userDto.getEmail()).role(Role.USER.name()).password(userDto.getPassword()).build();
        eventService.addUser(createUser);
        eventService.addAppUser(user);
        return userDto;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {
        return ResponseEntity.ok(authenticateService.getAuthToken(loginUser));
    }

    @GetMapping("/role")
    public ResponseEntity<String> getRole() {
        AppUser appUser = eventService.getAppUserFromToken();
        return ResponseEntity.ok(appUser.getRole());
    }

    @PostMapping("/event")
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto event) {
        AppUser appUser = eventService.getAppUserFromToken();
        eventService.addEvent(event, appUser);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id, eventService.getAppUserFromToken()));
    }

    @GetMapping("/event")
    public ResponseEntity<List<ReturnEventDto>> getEventsByDate(@RequestBody FindEventByDateDto eventDate) {
        return ResponseEntity.ok(eventService.getEvents(eventDate));
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<Event> deleteEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.deleteEventById(id, eventService.getAppUserFromToken()));
    }

    @PutMapping("/event/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody EventDto event) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventService.getAppUserFromToken(), event));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/report")
    public ResponseEntity<List<Event>> getReports() {
        return ResponseEntity.ok(eventService.getReports());
    }
}
