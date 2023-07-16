package ru.practicum.model;

import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;

public class MappingToStats {
    private MappingToStats() {

    }

    public static Stats mapToStats(EndpointHit endpointHit) {
        return Stats.builder()
                .uri(endpointHit.getUri())
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

    public static ViewStats mapToViewStats(Stats stats) {
        return ViewStats.builder()
                .uri(stats.getUri())
                .app(stats.getApp())
                .build();
    }
}
