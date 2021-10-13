package az.code.EventsApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class EmailAlreadyTakenException extends RuntimeException {
    public EmailAlreadyTakenException() {
        super("Email is already taken");
    }
}
