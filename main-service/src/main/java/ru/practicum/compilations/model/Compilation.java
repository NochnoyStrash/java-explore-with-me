package ru.practicum.compilations.model;

import lombok.*;
import ru.practicum.events.model.Event;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @JoinTable(
            name = "compilations_event",
            joinColumns = @JoinColumn(name = "comp_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;
    private boolean pinned;

    private String title;
}
