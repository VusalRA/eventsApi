package az.code.EventsApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdministratorDto {

    private String email;
    private String name;
    private String surname;
    private String password;

}
