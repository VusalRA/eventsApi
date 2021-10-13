package az.code.EventsApi.utils;

import az.code.EventsApi.repositories.AppUserRepo;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RegistrationValidator {

    private AppUserRepo appUserRepo;

    public RegistrationValidator(AppUserRepo appUserRepo) {
        this.appUserRepo = appUserRepo;
    }

    public boolean checkNameAndSurname(String text) {
        final Pattern NAME_REGEX = Pattern.compile("[A-Za-z].{2,33}$", Pattern.CASE_INSENSITIVE);
        return NAME_REGEX.matcher(text).matches();
    }

    public boolean checkEmailIsPresent(String email) {
        return appUserRepo.findByEmail(email).isPresent();
    }
}
