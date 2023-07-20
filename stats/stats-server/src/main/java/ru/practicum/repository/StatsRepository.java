package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ViewStats;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    @Query("SELECT NEW ru.practicum.ViewStats(s.app, s.uri, " +
            "CASE WHEN :unique = true THEN COUNT(DISTINCT s.ip) ELSE COUNT(s) END) " +
            "FROM Stats s " +
            "WHERE s.timestamp >= :start " +
            "AND s.timestamp <= :end " +
            "GROUP BY s.app, s.uri ")
    List<ViewStats> getViewStats(@Param("unique") boolean unique,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

    @Query("SELECT NEW ru.practicum.ViewStats(s.app, s.uri, " +
            "CASE WHEN :unique = true THEN COUNT(DISTINCT s.ip) ELSE COUNT(s) END as numb) " +
            "FROM Stats s " +
            "WHERE s.timestamp >= :start " +
            "AND s.timestamp <= :end " +
            "AND (s.uri IN :uris) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY numb desc")
    List<ViewStats> getViewStatsWithUris(@Param("unique") boolean unique,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end,
                                 @Param("uris") List<String> uris);

}
