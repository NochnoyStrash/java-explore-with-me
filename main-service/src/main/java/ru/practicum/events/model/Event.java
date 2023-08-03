package ru.practicum.events.model;

import lombok.*;
import ru.practicum.categories.model.Categories;
import ru.practicum.events.comments.model.Comment;
import ru.practicum.events.enums.State;
import ru.practicum.events.location.Location;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;
    @Column(name = "confirmed_Requests")
    private long confirmedRequests;
    @Column(name = "created_On")
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_Date")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    private boolean paid;
    @Column(name = "participant_Limit")
    private int participantLimit;
    @Column(name = "published_On")
    private LocalDateTime publishedOn;
    @Column(name = "request_Moderation")
    private boolean requestModeration;
    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private State state;
    private String title;
    private long views;
//    @Transient
//    private List<Comment> comments;
}
