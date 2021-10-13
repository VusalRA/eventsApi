package az.code.EventsApi.security;

import az.code.EventsApi.models.security.AuthToken;
import az.code.EventsApi.models.security.LoginUser;

public interface AuthenticateService {

    AuthToken getAuthToken(LoginUser loginUser);

}
