package az.code.EventsApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class IncorrectNameAndSurnameException extends RuntimeException {
    public IncorrectNameAndSurnameException() {
        super("Incorrect name or surname, please change and try again");
    }
}
