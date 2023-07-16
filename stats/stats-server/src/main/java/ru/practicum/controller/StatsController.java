package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class StatsController {
    private StatsService statsService;
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/stats")
    public List<ViewStats>  getStats(@RequestParam String start,
                                     @RequestParam String end,
                                     @RequestParam(required = false) List<String> uris,
                                     @RequestParam(defaultValue = "false") boolean unique) {
        if (uris == null) {
            uris = new ArrayList<>();
        }

        LocalDateTime startFromString = LocalDateTime.parse(start, format);
        LocalDateTime endFromString = LocalDateTime.parse(end, format);
        return statsService.getStats(startFromString, endFromString, uris, unique);
    }

    @PostMapping("/hit")
    public ResponseEntity<Void> saveHit(@RequestBody EndpointHit endpointHit) {
        statsService.saveHit(endpointHit);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
