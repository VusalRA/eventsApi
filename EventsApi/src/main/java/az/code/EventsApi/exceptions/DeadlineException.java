package az.code.EventsApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class DeadlineException extends RuntimeException{
    public DeadlineException(){
        super("You cannot enter the wrong date type or previous date");
    }
}
