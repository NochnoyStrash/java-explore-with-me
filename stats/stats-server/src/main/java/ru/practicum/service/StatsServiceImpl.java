package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.model.MappingToStats;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class StatsServiceImpl implements  StatsService {
    private StatsRepository statsRepository;

    @Override
    public void saveHit(EndpointHit endpointHit) {
        Stats stats = MappingToStats.mapToStats(endpointHit);
        statsRepository.save(stats);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris.isEmpty()) {
            return statsRepository.getViewStats(unique, start, end);
        }
        return statsRepository.getViewStatsWithUris(unique, start, end, uris);
    }
}
