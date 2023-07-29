package ru.practicum.requests.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.enums.State;
import ru.practicum.events.exceptions.EventConflictException;
import ru.practicum.events.exceptions.EventNotFoundException;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.requests.StatusRequest;
import ru.practicum.requests.exception.RequestNotFoundException;
import ru.practicum.requests.exception.RequestValidationException;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private RequestRepository requestRepository;
    private EventRepository eventRepository;
    private UserRepository userRepository;

    public Request saveRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с ID =  %d не найден", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EventNotFoundException(String.format("Событие с ID = %d не найдено", eventId)));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EventConflictException("Событие еще не опубликовано");
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new RequestValidationException("Запрос на участие не может делать создатель события");
        }
        List<Request> requests = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (!requests.isEmpty()) {
            throw new RequestValidationException("Запрос на участие на это событие уже отправлено");
        }
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .status(StatusRequest.PENDING)
                .build();
        if (event.getParticipantLimit() == 0) {
                request.setStatus(StatusRequest.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
                return requestRepository.save(request);
            } else if (!event.isRequestModeration()) {
                if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                    throw new EventConflictException("У этого события достигнут лимит участников");
                }
            request.setStatus(StatusRequest.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
            return requestRepository.save(request);
            } else {
            return requestRepository.save(request);
        }
    }

    public List<Request> getRequestsCurrentUser(Long userId) {
        return requestRepository.findByRequesterId(userId);
    }

    public Request canselRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new RequestNotFoundException(String.format("Запрос с таким ID = %d не найден", requestId)));
        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new RequestValidationException("Отменить запрос на участие может только сам создатель запроса");
        }
        if (request.getStatus().equals(StatusRequest.CONFIRMED)) {
            Long eventId = request.getEvent().getId();
            Event event = eventRepository.findById(eventId).orElseThrow(
                    () -> new EventNotFoundException(String.format("Событие с ID = %d не найдено", eventId)));
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
            request.setStatus(StatusRequest.CANCELED);
        } else {
            request.setStatus(StatusRequest.CANCELED);
        }
        return requestRepository.save(request);
    }
}
