package az.code.EventsApi.services;

import az.code.EventsApi.models.Administrator;
import az.code.EventsApi.models.AppUser;
import az.code.EventsApi.repositories.AdministratorRepo;
import az.code.EventsApi.repositories.AppUserRepo;
import az.code.EventsApi.services.interfaces.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Service(value = "eventService")
public class EventServiceImpl implements EventService, UserDetailsService {


    private AppUserRepo appUserRepo;
    private AdministratorRepo administratorRepo;
    private BCryptPasswordEncoder bcryptEncoder;

    public EventServiceImpl(AppUserRepo appUserRepo, AdministratorRepo administratorRepo, BCryptPasswordEncoder bcryptEncoder) {
        this.appUserRepo = appUserRepo;
        this.administratorRepo = administratorRepo;
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
}
