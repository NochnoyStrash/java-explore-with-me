package ru.practicum.requests.dto;

import ru.practicum.requests.model.Request;

public class RequestMapper {
    private RequestMapper() {

    }

    public static RequestDto getRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .requester(request.getRequester().getId())
                .build();
    }
}
