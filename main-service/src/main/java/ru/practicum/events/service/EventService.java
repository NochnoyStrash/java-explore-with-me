package ru.practicum.events.service;

import ru.practicum.events.comments.dto.NewCommentDto;
import ru.practicum.events.comments.model.Comment;
import ru.practicum.events.dto.*;
import ru.practicum.events.enums.State;
import ru.practicum.events.model.Event;
import ru.practicum.requests.model.Request;

import java.util.List;

public interface EventService {
    Event saveEvent(NewEventDto eventDto, Long userId);

    Event getEventById(Long eventId);

    Event getEventByIdPublic(Long eventId);

    List<Event> getEventsFromUser(Long userid, int from, int size);

    Event getEventFromUser(Long userId, Long eventId);

    Event updateEventFromUser(Long userId, Long eventId, UpdateEventUserRequest request);

    List<Request> getRequestOnEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeStatusRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest listRequest);

    List<EventDto> getEventsForAdmin(List<Long> users, List<State> states, List<Long> categories,
                                     String rangeStart, String rangeEnd, int from, int size);

    Event updateEventFromAdmin(Long eventId, UpdateEventAdminRequest request);

    List<Event> getEventsForUser(String text, List<Long> categories, Boolean paid, String rangeStart,
                                 String rangeEnd, String sort, Boolean onlyAvailable, int from, int size);

    Comment addComment(Long eventId, Long authorId, NewCommentDto commentDto);

    Comment updateComment(Long eventId, Long authorId, Long commId, NewCommentDto dto);

    public void deleteCommentFromAdmin(Long commId);

    public void deleteComment(Long authorId, Long eventId, Long commId);
}
