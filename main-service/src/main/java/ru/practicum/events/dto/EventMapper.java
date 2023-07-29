package ru.practicum.events.dto;

import ru.practicum.categories.model.CatMapper;
import ru.practicum.categories.model.dto.CatDto;
import ru.practicum.events.enums.State;
import ru.practicum.events.location.LocationDto;
import ru.practicum.events.model.Event;
import ru.practicum.user.dto.UserDtoShort;

import java.time.LocalDateTime;

public class EventMapper {
    private EventMapper() {

    }

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
        LocationDto location = LocationDto.builder()
                .lat(event.getLocation().getLat())
                .lon(event.getLocation().getLon())
                .build();
        UserDtoShort userDtoShort = new UserDtoShort(event.getInitiator().getId(),
                event.getInitiator().getName());
        CatDto catDto = CatMapper.getDtoFromCat(event.getCategory());
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(catDto)
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
                .build();
    }

    public static EventShortDto shortEventDto(Event event) {
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
                .build();
    }
}
