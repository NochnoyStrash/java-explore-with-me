package ru.practicum.events.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ViewStats;
import ru.practicum.categories.exception.CategoryNotFoundException;
import ru.practicum.categories.model.Categories;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.client.StatsClient;
import ru.practicum.events.comments.dto.CommentMapper;
import ru.practicum.events.comments.dto.NewCommentDto;
import ru.practicum.events.comments.model.Comment;
import ru.practicum.events.comments.repository.CommentsRepository;
import ru.practicum.events.dto.*;
import ru.practicum.events.enums.State;
import ru.practicum.events.enums.StateAdmin;
import ru.practicum.events.enums.StateUser;
import ru.practicum.events.exceptions.*;
import ru.practicum.events.location.Location;
import ru.practicum.events.location.LocationRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.requests.StatusRequest;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.requests.exception.RequestNotFoundException;
import ru.practicum.requests.exception.RequestValidationException;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.validate.ValidationClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;
    private final CommentsRepository commentsRepository;
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    @Transactional
    public Event saveEvent(NewEventDto eventDto, Long userId) {
        ValidationClass.validateNewEventDto(eventDto);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с ID = %d не найден", userId)));
        Categories categories = categoriesRepository.findById(eventDto.getCategory()).orElseThrow(
                () -> new CategoryNotFoundException(String.format("Категория с ID = %d не найден", eventDto.getCategory())));
        Location location = new Location();
        location.setLat(eventDto.getLocation().getLat());
        location.setLon(eventDto.getLocation().getLon());
        location = locationRepository.save(location);
        Event event = EventMapper.getEventFromDto(eventDto);
        event.setCategory(categories);
        event.setInitiator(user);
        event.setLocation(location);
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Comment addComment(Long eventId, Long authorId, NewCommentDto commentDto) {
        Event event = getEventById(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EventNotFoundException(String.format("Событие с ID =%d  не найдено", eventId));
        }
        if (authorId == null) {
            throw new ValidateCommentException("Комментарии могут оставлять только зарегистрированные пользователи");
        }
        User author = userRepository.findById(authorId).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с ID = %d не найден", authorId)));
        String text = commentDto.getText();
        Comment comment = CommentMapper.getComment(text, event, author);
        return commentsRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment updateComment(Long eventId, Long authorId,  Long commId, NewCommentDto dto) {
        Event event = getEventById(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EventNotFoundException(String.format("Событие с ID =%d  не найдено", eventId));
        }
        if (authorId == null) {
            throw new ValidateCommentException("Комментарии могут оставлять только зарегистрированные пользователи");
        }
        Comment comment = commentsRepository.findById(commId).orElseThrow(
                () -> new CommentNotFoundException(String.format("Комментарий с ID = %d  не найден", commId)));
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new ValidateCommentException("Обновить комментарий может только его создатель");
        }
        comment.setText(dto.getText());
        comment.setCreated(LocalDateTime.now());
        return commentsRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteCommentFromAdmin(Long commId) {
        Comment comment = commentsRepository.findById(commId).orElseThrow(
                () -> new CommentNotFoundException(String.format("Комментарий с ID = %d  не найден", commId)));
        commentsRepository.deleteById(commId);
    }

    @Override
    @Transactional
    public void deleteComment(Long authorId, Long eventId, Long commId) {
        Event event = getEventById(eventId);
        Comment comment = commentsRepository.findById(commId).orElseThrow(
                () -> new CommentNotFoundException(String.format("Комментарий с ID = %d  не найден", commId)));
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new ValidateCommentException("Удалить комментарий может только его создатель или администратор");
        }
        commentsRepository.deleteById(commId);
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EventNotFoundException(String.format("Событие с ID = %d не найдено", eventId)));
    }

    @Override
    @Transactional
    public Event getEventByIdPublic(Long eventId) {
        Event event = getEventById(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EventNotFoundException(String.format("Событие с ID =%d  не найдено", eventId));
        }
        String uri = "/events/" + eventId;
        Long views = getViews(uri, event.getCreatedOn(), LocalDateTime.now());
        event.setViews(views);
        event = eventRepository.save(event);
        List<Comment> comments = commentsRepository.findByEventId(eventId);
        event.setComments(comments);
        return event;
    }


    @Override
    public List<Event> getEventsForUser(String text, List<Long> categories, Boolean paid, String rangeStart,
                                        String rangeEnd, String sort, Boolean onlyAvailable, int from, int size) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, FORMAT);
        } else {
            start = LocalDateTime.now();
        }
        if (rangeEnd !=  null) {
            end = LocalDateTime.parse(rangeEnd, FORMAT);
        } else {
            end = LocalDateTime.of(9999, 12, 31, 0, 0);
        }
        int page = from / size;
        int pageLast = from % size;
        if (pageLast > 0) {
            page++;
        }
        if (start.isAfter(end)) {
            throw new EventValidateException("дата отсчета позже чем дата конца");
        }
        if (onlyAvailable) {
            return eventRepository.getEventsPublicLimit(text, categories, paid, start, end, sort, PageRequest.of(page, size));
        }
        return eventRepository.getEventsPublic(text, categories, paid, start, end, sort, PageRequest.of(page, size));
    }

    @Override
    public List<Event> getEventsFromUser(Long userId, int from, int size) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с ID = %d не найден", userId)));

        int page = from / size;
        int pageLast = from % size;
        if (pageLast > 0) {
            page++;
        }
        return eventRepository.findByInitiatorId(userId, PageRequest.of(page, size));
    }

    @Override
    public Event getEventFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с ID = %d не найден", userId)));

        return getEventById(eventId);
    }

    @Override
    @Transactional
    public Event updateEventFromUser(Long userId, Long eventId, UpdateEventUserRequest request) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с ID = %d не найден", userId)));
        Event event = getEventById(eventId);
        update(event, request);
        return eventRepository.save(event);

    }

    @Override
    @Transactional
    public Event updateEventFromAdmin(Long eventId, UpdateEventAdminRequest request) {
        Event event = getEventById(eventId);
        updateFromAdmin(event, request);
        return eventRepository.save(event);
    }

    @Override
    public List<Request> getRequestOnEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с ID = %d не найден", userId)));
        Event event = getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EventValidateException("Информацию может запрашивать только создатель события");
        }
        return requestRepository.findByEventId(eventId);
    }

    @Override
    public List<EventDto> getEventsForAdmin(List<Long> users, List<State> states, List<Long> categories,
                                            String rangeStart, String rangeEnd, int from, int size) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, FORMAT);
        } else {
            start = LocalDateTime.now();
        }
        if (rangeEnd !=  null) {
            end = LocalDateTime.parse(rangeEnd, FORMAT);
        } else {
            end = LocalDateTime.of(9999, 12, 31, 0, 0);
        }
        int page = from / size;
        int pageLast = from % size;
        if (pageLast > 0) {
            page++;
        }
        List<Event> events = eventRepository.getEventsForAdmin(users, states, categories, start, end, PageRequest.of(page, size));
        return events.stream().map(EventMapper::getEventDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeStatusRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest listRequest) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с ID = %d не найден", userId)));
        Event event = getEventById(eventId);
        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            confirmedRequests = requestRepository.findByEventId(eventId).stream().map(RequestMapper::getRequestDto).collect(Collectors.toList());
            return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
        }
        for (Long id : listRequest.getRequestIds()) {
            Request request = requestRepository.findById(id).orElseThrow(() -> new RequestNotFoundException(String.format("Запрос с таким ID = %d не найден", id)));
            if (!request.getStatus().equals(StatusRequest.PENDING)) {
                throw new RequestValidationException("Изменить статус заявки можно только заявки в режиме ожидания");
            }
            if (listRequest.getStatus().equals(StatusRequest.CONFIRMED)) {
                if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
                    throw new EventConflictException("Невозможно подтвердить так как достигнут лимит по количеству участинков");
                }
                request.setStatus(StatusRequest.CONFIRMED);
                request = requestRepository.save(request);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                event = eventRepository.save(event);
                RequestDto requestDto = RequestMapper.getRequestDto(request);
                confirmedRequests.add(requestDto);
            } else if (listRequest.getStatus().equals(StatusRequest.REJECTED)) {
                request.setStatus(StatusRequest.REJECTED);
                request = requestRepository.save(request);
                RequestDto requestDto = RequestMapper.getRequestDto(request);
                rejectedRequests.add(requestDto);

            }
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Transactional
    private void updateFromAdmin(Event event, UpdateEventAdminRequest request) {
        if (!event.getState().equals(State.PENDING)) {
            throw new EventConflictException("Нельзя изменить опубликованное событие");
        }
        if (request.getEventDate() != null) {
            if (request.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new EventValidateException("Время для события должно быть не раньше чем за 1 час от текущего времени");
            }
            event.setEventDate(request.getEventDate());
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            Categories categories = categoriesRepository.findById(request.getCategory()).orElseThrow(
                    () -> new CategoryNotFoundException(String.format("Категория с ID = %d не найдена", request.getCategory())));
            event.setCategory(categories);
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getLocation() != null) {
            locationRepository.delete(event.getLocation());
            Location location = Location.builder()
                    .lon(request.getLocation().getLon())
                    .lat(request.getLocation().getLat())
                    .build();
            location = locationRepository.save(location);
            event.setLocation(location);
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            if (request.getStateAction().equals(StateAdmin.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else {
                event.setState(State.CANCELED);
            }
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
    }


    @Transactional
    private void update(Event event, UpdateEventUserRequest request) {
        if (event.getState().equals(State.PUBLISHED)) {
            throw new EventConflictException("Нельзя изменить опубликованное событие");
        }
        if (request.getEventDate() != null) {
            if (request.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new EventValidateException("Время для события должно быть не раньше чем через 2 часа от текущего времени");
            }
            event.setEventDate(request.getEventDate());
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            Categories categories = categoriesRepository.findById(request.getCategory()).orElseThrow(
                    () -> new CategoryNotFoundException(String.format("Категория с ID = %d не найдена", request.getCategory())));
            event.setCategory(categories);
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getLocation() != null) {
            locationRepository.delete(event.getLocation());
            Location location = Location.builder()
                    .lon(request.getLocation().getLon())
                    .lat(request.getLocation().getLat())
                    .build();
            location = locationRepository.save(location);
            event.setLocation(location);
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            if (request.getStateAction().equals(StateUser.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PENDING);
            }
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
    }

    private Long getViews(String uri, LocalDateTime start, LocalDateTime end) {
        return Optional.ofNullable(statsClient.getStats(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), List.of(uri), true))
                .map(ResponseEntity::getBody)
                .stream()
                .flatMap(Collection::stream)
                .filter(viewStats -> viewStats.getUri().equals(uri))
                .mapToLong(ViewStats::getHits)
                .sum();
    }



}
