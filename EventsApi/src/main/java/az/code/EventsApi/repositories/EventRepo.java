package az.code.EventsApi.repositories;

import az.code.EventsApi.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EventRepo extends JpaRepository<Event, Long> {

    Event findByDate(LocalDate Date);

    @Query("SELECT event from Event event where event.date>=:from and event.date <=:to")
    List<Event> findByDateBetween(LocalDate from, LocalDate to);

}
