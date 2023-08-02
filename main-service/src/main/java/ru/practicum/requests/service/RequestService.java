package ru.practicum.requests.service;

import ru.practicum.requests.model.Request;

import java.util.List;

public interface RequestService {
    Request saveRequest(Long userId, Long eventId);

    Request canselRequest(Long userId, Long requestId);

    List<Request> getRequestsCurrentUser(Long userId);
}
