package ru.practicum.events;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHit;
import ru.practicum.events.comments.dto.CommentDto;
import ru.practicum.events.comments.dto.CommentMapper;
import ru.practicum.events.comments.dto.NewCommentDto;
import ru.practicum.events.comments.model.Comment;
import ru.practicum.events.dto.*;
import ru.practicum.events.enums.State;
import ru.practicum.events.model.Event;
import ru.practicum.events.service.EventService;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.stats.StatsClientTree;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
public class EventsController {
    private final EventService eventService;
    private final StatsClientTree statsClientTree;
    private static final String APPLICATION = "ewm-main-service";
    private static final String USER_ID_HEADERS = "X-Sharer-User-Id";

    @GetMapping("/events/{id}")
    public EventDto getEventById(@PathVariable long id, HttpServletRequest request) {
        Event event = eventService.getEventByIdPublic(id);
        EndpointHit endpointHit = EndpointHit.builder()
                .app(APPLICATION)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
            statsClientTree.saveStats(endpointHit);
        return EventMapper.getEventDto(event);
    }

//    @PostMapping("/events/{id}/comments")
//    @ResponseStatus(HttpStatus.CREATED)
//    public CommentDto addComment(@RequestHeader(USER_ID_HEADERS) Long authorId,
//                                 @PathVariable long id,
//                                 @RequestBody @Valid NewCommentDto dto) {
//        Comment comment = eventService.addComment(id, authorId, dto);
//        return CommentMapper.mapToCommentDto(comment);
//    }
//
//    @PatchMapping("/events/{id}/comments/{commId}")
//    public CommentDto updateComment(@RequestHeader(USER_ID_HEADERS) Long authorId,
//                                 @PathVariable long id,
//                                 @PathVariable long commId,
//                                 @RequestBody @Valid NewCommentDto dto) {
//        Comment comment = eventService.updateComment(id, authorId, commId, dto);
//        return CommentMapper.mapToCommentDto(comment);
//    }
//
//    @DeleteMapping("/admin/comments/{commId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteCommentsFromAdmin(@PathVariable Long commId) {
//        eventService.deleteCommentFromAdmin(commId);
//    }
//
//    @DeleteMapping("/events/{id}/comments/{commId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteComment(@RequestHeader(USER_ID_HEADERS) Long authorId,
//                              @PathVariable long id,
//                              @PathVariable long commId) {
//        eventService.deleteComment(authorId, id, commId);
//    }


    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto saveEvent(@PathVariable long userId, @RequestBody @Valid NewEventDto dto) {
        Event event = eventService.saveEvent(dto, userId);
        return EventMapper.getEventDto(event);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsFromUser(@PathVariable long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsFromUser(userId, from, size).stream().map(EventMapper::shortEventDto).collect(Collectors.toList());
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventDto getEventFromUser(@PathVariable long userId,
                                  @PathVariable long eventId) {
        Event event = eventService.getEventFromUser(userId,eventId);
        return EventMapper.getEventDto(event);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDto updateEventFromUser(@PathVariable long userId,
                                     @PathVariable long eventId,
                                     @RequestBody @Valid UpdateEventUserRequest request) {
        Event event = eventService.updateEventFromUser(userId, eventId, request);
        return EventMapper.getEventDto(event);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestOnEvent(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.getRequestOnEvent(userId, eventId).stream().map(RequestMapper::getRequestDto).collect(Collectors.toList());
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatus(@PathVariable long userId, @PathVariable long eventId,
                                                       @RequestBody @Valid EventRequestStatusUpdateRequest request) {
        return eventService.changeStatusRequest(userId, eventId, request);
    }

    @GetMapping("/admin/events")
    public List<EventDto> getEventForAdmin(@RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) List<State> states,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventDto updateEventFromAdmin(@PathVariable long eventId, @RequestBody @Valid UpdateEventAdminRequest request) {
        Event event = eventService.updateEventFromAdmin(eventId, request);
        return EventMapper.getEventDto(event);
    }

    @GetMapping("/events")
    public List<EventShortDto> getEventForUser(@RequestParam(defaultValue = "") String text,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) Boolean paid,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                        @RequestParam(defaultValue = "VIEWS") String sort,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app(APPLICATION)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now()).build();
            statsClientTree.saveStats(endpointHit);

        return eventService.getEventsForUser(text, categories, paid, rangeStart,
                rangeEnd, sort, onlyAvailable, from, size).stream().map(EventMapper::shortEventDto).collect(Collectors.toList());
    }


}
