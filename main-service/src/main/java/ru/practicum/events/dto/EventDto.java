package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.categories.model.dto.CategoryDto;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.events.enums.State;
import ru.practicum.events.location.LocationDto;
import ru.practicum.user.dto.UserDtoShort;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Long id;
    private String annotation;
    private CategoryDto category;
    private long confirmedRequests;
    @JsonFormat(pattern = FORMAT)
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = FORMAT)
    private LocalDateTime eventDate;
    private UserDtoShort initiator;
    private LocationDto location;
    private boolean paid;
    private int participantLimit;
    @JsonFormat(pattern = FORMAT)
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private State state;
    private String title;
    private List<CommentDto> comments;
    private long views;
}
