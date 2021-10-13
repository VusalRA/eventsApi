package az.code.EventsApi.utils;

import az.code.EventsApi.exceptions.EmailNotValidException;
import az.code.EventsApi.exceptions.PasswordNotValidException;
import az.code.EventsApi.repositories.AppUserRepo;
import org.springframework.stereotype.Component;

@Component
public class LoginValidator {

    private EmailValidator emailValidator;
    private PasswordValidator passwordValidator;
    private AppUserRepo appUserRepo;

    public LoginValidator(EmailValidator emailValidator, PasswordValidator passwordValidator, AppUserRepo appUserRepo) {
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
        this.appUserRepo = appUserRepo;
    }

    public void checkEmailAndPassword(String email, String password) {
        boolean isValidEmail = emailValidator.
                test(email);

        if (!isValidEmail) {
            throw new EmailNotValidException();
        }

        boolean isValidPassword = passwordValidator.
                test(password);
        if (!isValidPassword) {
            throw new PasswordNotValidException();
        }
    }

}
