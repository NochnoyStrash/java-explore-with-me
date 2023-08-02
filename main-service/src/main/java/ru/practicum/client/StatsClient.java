package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.stats.StatsClientTree;


@Service
public class StatsClient extends StatsClientTree {

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String url) {
        super(url);
    }
}
