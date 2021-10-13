package az.code.EventsApi.services;

import az.code.EventsApi.dto.EventDto;
import az.code.EventsApi.dto.FindEventByDateDto;
import az.code.EventsApi.dto.ReturnEventDto;
import az.code.EventsApi.enums.ColorTitleBar;
import az.code.EventsApi.enums.EventType;
import az.code.EventsApi.enums.Role;
import az.code.EventsApi.exceptions.DeadlineException;
import az.code.EventsApi.models.Administrator;
import az.code.EventsApi.models.AppUser;
import az.code.EventsApi.models.Event;
import az.code.EventsApi.repositories.AdministratorRepo;
import az.code.EventsApi.repositories.AppUserRepo;
import az.code.EventsApi.repositories.EventRepo;
import az.code.EventsApi.services.interfaces.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service(value = "eventService")
public class EventServiceImpl implements EventService, UserDetailsService {


    private AppUserRepo appUserRepo;
    private AdministratorRepo administratorRepo;
    private EventRepo eventRepo;
    private BCryptPasswordEncoder bcryptEncoder;

    public EventServiceImpl(AppUserRepo appUserRepo, AdministratorRepo administratorRepo, EventRepo eventRepo, BCryptPasswordEncoder bcryptEncoder) {
        this.appUserRepo = appUserRepo;
        this.administratorRepo = administratorRepo;
        this.eventRepo = eventRepo;
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Invalid email or password."
                ));
        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(), getAuthority(appUser));
    }

    private Set<SimpleGrantedAuthority> getAuthority(AppUser appUser) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + appUser.getRole()));
        return authorities;
    }

    @Override
    public Administrator addAdministrator(Administrator administrator) {
        return administratorRepo.save(administrator);
    }

    @Override
    public AppUser addAppUser(AppUser appUser) {
        appUser.setPassword(bcryptEncoder.encode(appUser.getPassword()));
        return appUserRepo.save(appUser);
    }

    public AppUser getAppUserFromToken() {
        String tokenPayload = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        String token = tokenPayload.substring(7, tokenPayload.length());
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readValue(payload, JsonNode.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String email = jsonNode.get("sub").asText();
        return appUserRepo.findByEmail(email).get();
    }

    @Override
    public Event addEvent(EventDto event, AppUser user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = null;
        try {
            dateTime = LocalDate.parse(event.getDate(), formatter);
            if (LocalDate.now().isAfter(dateTime)) {
                throw new DeadlineException();
            }
        } catch (Exception e) {
            throw new DeadlineException();
        }

        Event addEvent = Event.builder().description(event.getDescription()).createdBy(
                user.getEmail()
        ).title(event.getTitle()).type(event.getType().name()).date(dateTime).build();

        if (event.getType().equals(EventType.REST)) {
            addEvent.setColorTitleBar(ColorTitleBar.GREEN);
        } else if (event.getType().equals(EventType.HOLIDAY)) {
            addEvent.setColorTitleBar(ColorTitleBar.GREEN);
        } else {
            Long days = calculateDays(dateTime);
            addEvent.setColorTitleBar(getColor(days));
        }


        return eventRepo.save(addEvent);
    }


    @Override
    public Event getEvent(Long id, AppUser user) {
        if (eventRepo.findById(id).isPresent()) {
            Event event = eventRepo.findById(id).get();
            if (!event.getReadList().contains(user)) {
                event.getReadList().add(user);
            }
            eventRepo.save(event);
            return eventRepo.findById(id).get();
        } else {
            throw new RuntimeException("Event not found");
        }
    }

    @Override
    public List<ReturnEventDto> getEvents(FindEventByDateDto date) {
        List<ReturnEventDto> returnEventDtos = new ArrayList<>();
        List<Event> events = new ArrayList<>();

        if (date.getDate().length() > 10) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateTimeFrom = null;
            LocalDate dateTimeTo = null;
            dateTimeFrom = LocalDate.parse(date.getDate().substring(0, 10), formatter);
            dateTimeTo = LocalDate.parse(date.getDate().substring(11, date.getDate().length()), formatter);
            eventRepo.findByDateBetween(dateTimeFrom, dateTimeTo).stream().forEach(event -> events.add(event));
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateTime = null;
            dateTime = LocalDate.parse(date.getDate(), formatter);
            events.add(eventRepo.findByDate(dateTime));
        }

        events.stream().forEach(event -> returnEventDtos.add(new ReturnEventDto(event, getAppUserFromToken())));

        return returnEventDtos;
    }

    @Override
    public Event deleteEventById(Long id, AppUser user) {
        Event event = eventRepo.findById(id).get();
        if (user.getRole().equals(Role.ADMIN.name())) {
            eventRepo.delete(event);
        } else {
            if (event.getCreatedBy().equals(user.getEmail())) {
                eventRepo.delete(event);
            }
        }
        return event;
    }

    @Override
    public Event changeEvent(Long id, AppUser user, EventDto event) {
//        Event event
        Event eventById = eventRepo.findById(id).get();
//        if (user.getRole().equals(Role.ADMIN.name())) {
//            eventRepo.save(event);
//        } else {
//            if (event.getCreatedBy().equals(user.getEmail())) {
//                eventRepo.save(event);
//            }
//        }

        return eventById;
    }

    private Long calculateDays(LocalDate to) {
        return ChronoUnit.DAYS.between(LocalDate.now(), to);
    }

    @Override
    @Scheduled(cron = "0 1 * * * ?")
    public void checkDate() {
        List<Event> events = eventRepo.findAll();
        events.stream().forEach(i -> {
            if (!i.getColorTitleBar().equals(ColorTitleBar.GREEN)) {
                i.setColorTitleBar(getColor(calculateDays(i.getDate())));
                eventRepo.save(i);
            }
        });
    }

    private ColorTitleBar getColor(Long days) {
        ColorTitleBar colorTitleBar = null;

        if (days >= 21) {
            colorTitleBar = ColorTitleBar.BLUE;
        } else if (days <= 5) {
            colorTitleBar = ColorTitleBar.RED;
        } else if (days >= 5 && days <= 10) {
            colorTitleBar = ColorTitleBar.ORANGE;
        } else if (days >= 10 && days <= 20) {
            colorTitleBar = ColorTitleBar.YELLOW;
        }
        return colorTitleBar;
    }
}
