package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.service.StatsService;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class StatsController {
    private StatsService statsService;

    @GetMapping("/stats")
    public List<ViewStats>  getStats(@RequestParam String start,
                                     @RequestParam String end,
                                     @RequestParam(required = false) List<String> uris,
                                     @RequestParam(defaultValue = "false") boolean unique) {
        if (uris == null) {
            uris = new ArrayList<>();
        }
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> saveHit(@RequestBody EndpointHit endpointHit) {
        statsService.saveHit(endpointHit);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
