package ru.practicum.events.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.requests.StatusRequest;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private StatusRequest status;
}
