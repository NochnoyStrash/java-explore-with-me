package ru.practicum.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.events.enums.State;
import ru.practicum.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByInitiatorId(Long initiatorId, Pageable pageable);

    @Query("select e from Event e " +
            "join e.initiator as user " +
            "join e.category as cat " +
            "where (:users IS NULL OR user.id IN (:users)) " +
            "AND (:states IS NULL OR e.state IN (:states)) " +
            "AND (:categories IS NULL OR cat.id IN (:categories)) " +
            "AND  e.eventDate > :rangeStart " +
            "AND  e.eventDate < :rangeEnd ")
    List<Event> getEventsForAdmin(@Param("users") List<Long> users,
                          @Param("states") List<State> states,
                          @Param("categories") List<Long> categories,
                          @Param("rangeStart") LocalDateTime rangeStart,
                          @Param("rangeEnd") LocalDateTime rangeEnd,
                          Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "JOIN e.category as cat " +
            "WHERE (lower(e.annotation) like lower(concat('%', :text, '%') )  OR " +
            "lower(e.description) like lower(concat('%', :text, '%') )) " +
            "AND e.state = 'PUBLISHED' " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:categories IS NULL OR cat.id IN (:categories)) " +
            "AND e.eventDate > :start " +
            "AND e.eventDate < :end " +
            "AND (e.confirmedRequests < e.participantLimit)  " +
            "ORDER BY CASE WHEN :sort = 'EVENT_DATE' THEN e.eventDate END DESC, " +
            "CASE WHEN :sort = 'VIEWS' THEN e.views END DESC ")
    List<Event> getEventsPublicLimit(@Param("text") String text,
                                @Param("categories") List<Long> categories,
                                @Param("paid") Boolean paid,
                                @Param("start") LocalDateTime rangeStart,
                                @Param("end") LocalDateTime rangeEnd,
                                @Param("sort") String sort,
                                Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "JOIN e.category as cat " +
            "WHERE (lower(e.annotation) like lower(concat('%', :text, '%') )  OR " +
            "lower(e.description) like lower(concat('%', :text, '%') )) " +
            "AND e.state = 'PUBLISHED' " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:categories IS NULL OR cat.id IN (:categories)) " +
            "AND e.eventDate > :start " +
            "AND e.eventDate < :end " +
            "ORDER BY CASE WHEN :sort = 'EVENT_DATE' THEN e.eventDate END DESC, " +
            "CASE WHEN :sort = 'VIEWS' THEN e.views END DESC ")
    List<Event> getEventsPublic(@Param("text") String text,
                                @Param("categories") List<Long> categories,
                                @Param("paid") Boolean paid,
                                @Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end,
                                @Param("sort") String sort,
                                Pageable pageable);
}
