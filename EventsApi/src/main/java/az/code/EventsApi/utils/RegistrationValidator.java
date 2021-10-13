package az.code.EventsApi.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RegistrationValidator {

    public boolean checkNameAndSurname(String text) {
        final Pattern NAME_REGEX = Pattern.compile("[A-Za-z].{2,33}$", Pattern.CASE_INSENSITIVE);
        return NAME_REGEX.matcher(text).matches();
    }
}
