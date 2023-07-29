package ru.practicum.compilations.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.stats.StatsClientTree;


@Service
public class StatsClient extends StatsClientTree {
    private static final String SERVER_URI = "http://localhost:9090";

    @Autowired
    public StatsClient(@Value("http://localhost:9090") String url) {
        super(url);
    }
}
