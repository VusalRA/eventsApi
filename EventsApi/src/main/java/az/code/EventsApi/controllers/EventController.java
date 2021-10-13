package az.code.EventsApi.controllers;

import az.code.EventsApi.dto.AdministratorDto;
import az.code.EventsApi.enums.Role;
import az.code.EventsApi.models.Administrator;
import az.code.EventsApi.models.AppUser;
import az.code.EventsApi.models.security.LoginUser;
import az.code.EventsApi.security.AuthenticateService;
import az.code.EventsApi.services.interfaces.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("api/v1")
public class EventController {


    private AuthenticateService authenticateService;
    private EventService eventService;

    public EventController(AuthenticateService authenticateService, EventService eventService) {
        this.authenticateService = authenticateService;
        this.eventService = eventService;
    }

    @PostMapping("/register")
    public AdministratorDto saveAdministrator(@RequestBody AdministratorDto administrator) {
//        if (!registrationValidator.checkNameAndSurname(organization.getName())) {
//            throw new IncorrectNameAndSurnameException();
//        }
//
        Administrator createAdministrator = Administrator.builder().name(administrator.getName()).surname(administrator.getSurname()).build();
        AppUser user = AppUser.builder().email(administrator.getEmail()).password(administrator.getPassword()).role(Role.ADMIN.name()).build();
        eventService.addAdministrator(createAdministrator);
        eventService.addAppUser(user);
        return administrator;
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

}
