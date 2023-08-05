package ru.practicum.events.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.categories.model.CategoryMapper;
import ru.practicum.categories.model.dto.CategoryDto;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentMapper;
import ru.practicum.events.enums.State;
import ru.practicum.events.location.LocationDto;
import ru.practicum.events.model.Event;
import ru.practicum.user.dto.UserDtoShort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {

    public static Event getEventFromDto(NewEventDto dto) {
        boolean paid = false;
        boolean requestModeration = true;
        if (dto.getPaid() != null) {
            paid = dto.getPaid();
        }
        if (dto.getRequestModeration() != null) {
            requestModeration = dto.getRequestModeration();
        }
        return Event.builder()
                .annotation(dto.getAnnotation())
                .createdOn(LocalDateTime.now())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .paid(paid)
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(requestModeration)
                .state(State.PENDING)
                .title(dto.getTitle())
                .build();
    }

    public static EventDto getEventDto(Event event) {
        List<CommentDto> commentDtos = new ArrayList<>();
        if (event.getComments() != null) {
            commentDtos = event.getComments().stream().map(CommentMapper::mapToCommentDto).collect(Collectors.toList());
        }
        LocationDto location = LocationDto.builder()
                .lat(event.getLocation().getLat())
                .lon(event.getLocation().getLon())
                .build();
        UserDtoShort userDtoShort = new UserDtoShort(event.getInitiator().getId(),
                event.getInitiator().getName());
        CategoryDto categoryDto = CategoryMapper.getDtoFromCategory(event.getCategory());
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryDto)
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(userDtoShort)
                .location(location)
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .comments(commentDtos)
                .build();
    }

    public static EventShortDto shortEventDto(Event event) {
        List<CommentDto> commentDtos = new ArrayList<>();
        if (event.getComments() != null) {
            commentDtos = event.getComments().stream().map(CommentMapper::mapToCommentDto).collect(Collectors.toList());
        }
        return EventShortDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .initiator(event.getInitiator())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .paid(event.isPaid())
                .title(event.getTitle())
                .comments(commentDtos)
                .build();
    }
}
