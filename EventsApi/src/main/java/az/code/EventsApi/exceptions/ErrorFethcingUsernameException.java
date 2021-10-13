package az.code.EventsApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ErrorFethcingUsernameException extends RuntimeException {
    public ErrorFethcingUsernameException() {
        super("An error occurred while fetching Username from Token");
    }
}
