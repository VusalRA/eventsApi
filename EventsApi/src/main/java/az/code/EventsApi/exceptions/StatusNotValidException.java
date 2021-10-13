package az.code.EventsApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class StatusNotValidException extends RuntimeException {
    public StatusNotValidException(){
        super("Status does not exist or you entered in lower case");
    }
}
