package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.model.MappingToStats;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements  StatsService {
    private StatsRepository statsRepository;
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public void saveHit(EndpointHit endpointHit) {
        Stats stats = MappingToStats.mapToStats(endpointHit);
        statsRepository.save(stats);
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startFromString = LocalDateTime.parse(start, format);
        LocalDateTime endFromString = LocalDateTime.parse(end, format);
        if(startFromString.isBefore(endFromString)) {
            throw new  RuntimeException("дата старта позже чем дата конца");
        }
        if (uris.isEmpty()) {
            return statsRepository.getViewStats(unique, startFromString, endFromString);
        }
        return statsRepository.getViewStatsWithUris(unique, startFromString, endFromString, uris);
    }
}
