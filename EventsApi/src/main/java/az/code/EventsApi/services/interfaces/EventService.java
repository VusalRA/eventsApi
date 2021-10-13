package az.code.EventsApi.services.interfaces;

import az.code.EventsApi.models.Administrator;
import az.code.EventsApi.models.AppUser;

public interface EventService {

    Administrator addAdministrator(Administrator administrator);

    AppUser addAppUser(AppUser appUser);

    AppUser getAppUserFromToken();

}
