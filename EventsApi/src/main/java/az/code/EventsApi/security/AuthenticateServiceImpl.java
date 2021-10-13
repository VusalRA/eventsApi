package az.code.EventsApi.security;

import az.code.EventsApi.models.security.AuthToken;
import az.code.EventsApi.models.security.LoginUser;
import az.code.EventsApi.utils.LoginValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateServiceImpl implements AuthenticateService {

    private TokenProvider jwtTokenUtil;
    private AuthenticationManager authenticationManager;
    private LoginValidator loginValidator;

    public AuthenticateServiceImpl(TokenProvider jwtTokenUtil, AuthenticationManager authenticationManager, LoginValidator loginValidator) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.loginValidator = loginValidator;
    }

    public AuthToken getAuthToken(LoginUser loginUser) {
        loginValidator.checkEmailAndPassword(loginUser.getEmail(), loginUser.getPassword());
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return new AuthToken(token);
    }

}
