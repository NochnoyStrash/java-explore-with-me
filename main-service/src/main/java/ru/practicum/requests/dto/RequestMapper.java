package ru.practicum.requests.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.requests.model.Request;

@UtilityClass
public class RequestMapper {

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
