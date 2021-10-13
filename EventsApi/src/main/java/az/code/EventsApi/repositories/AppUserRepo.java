package az.code.EventsApi.repositories;

import az.code.EventsApi.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {

    //    @Query("SELECT appUser FROM AppUser appUser where appUser.")
    Optional<AppUser> findByEmail(String Email);

}
