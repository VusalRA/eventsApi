package az.code.EventsApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class PasswordNotValidException extends RuntimeException {
    public PasswordNotValidException() {
        super("Password is not valid");
    }
}
