package az.code.EventsApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class EmailNotValidException extends RuntimeException {
    public EmailNotValidException() {
        super("Email is not valid");
    }
}
