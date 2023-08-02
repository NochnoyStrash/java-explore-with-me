package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(Long userId);

    List<Request> findByEventId(Long eventId);

    List<Request> findByRequesterIdAndEventId(Long requesterId, Long eventId);
}
