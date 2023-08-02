package ru.practicum.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;

import java.util.List;

@Slf4j
public class StatsClientTree {
    private final WebClient client;

    public StatsClientTree(String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public void saveStats(EndpointHit hit) {
        this.client.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(hit)
                .retrieve()
                .toBodilessEntity()
                .doOnNext(voidResponseEntity -> log.info("Просмотр сохранен"))
                .block();
    }

    public ResponseEntity<List<ViewStats>> getStats(String start, String end, List<String> uris, Boolean unique) {
        return this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(ViewStats.class)
                .doOnNext(listResponseEntity -> log.info("получение статистики"))
                .block();
    }

}
