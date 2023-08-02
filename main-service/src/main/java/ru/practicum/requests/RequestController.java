package ru.practicum.requests;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.service.RequestService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Validated
public class RequestController {
    private RequestService requestService;

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> getRequestsCurrentUser(@PathVariable long userId) {
        return requestService.getRequestsCurrentUser(userId).stream()
                .map(RequestMapper::getRequestDto).collect(Collectors.toList());

    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto saveRequest(@PathVariable long userId,
                                  @RequestParam @NotNull Long eventId) {
        Request request = requestService.saveRequest(userId, eventId);
        return RequestMapper.getRequestDto(request);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto canselRequest(@PathVariable long userId,
                                    @PathVariable long requestId) {
        Request request = requestService.canselRequest(userId, requestId);
        return RequestMapper.getRequestDto(request);
    }



}
