package az.code.EventsApi.repositories;

import az.code.EventsApi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
