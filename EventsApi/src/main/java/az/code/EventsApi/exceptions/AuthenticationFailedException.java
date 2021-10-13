package az.code.EventsApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException() {
        super("Authentication Failed. Username or Password not valid.");
    }
}
