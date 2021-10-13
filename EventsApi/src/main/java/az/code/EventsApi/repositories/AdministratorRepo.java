package az.code.EventsApi.repositories;

import az.code.EventsApi.models.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepo extends JpaRepository<Administrator, Long> {
}
