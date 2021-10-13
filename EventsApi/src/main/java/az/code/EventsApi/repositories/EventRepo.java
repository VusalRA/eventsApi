package az.code.EventsApi.repositories;

import az.code.EventsApi.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event, Long> {

}
