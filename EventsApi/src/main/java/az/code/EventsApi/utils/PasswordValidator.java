package az.code.EventsApi.utils;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class PasswordValidator implements Predicate<String> {
    @Override
    public boolean test(String password) {
        final Pattern PASSWORD_REGEX = Pattern.compile("[A-Za-z0-9].{6,33}$", Pattern.CASE_INSENSITIVE);
        return PASSWORD_REGEX.matcher(password).matches();
    }
}
