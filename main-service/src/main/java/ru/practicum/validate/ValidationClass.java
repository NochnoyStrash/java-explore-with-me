package ru.practicum.validate;

import lombok.experimental.UtilityClass;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.exceptions.EventValidateException;

import java.time.LocalDateTime;

@UtilityClass
public class ValidationClass {

    public static void validateNewEventDto(NewEventDto dto) {
        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventValidateException("Время для события должно быть не раньше чем через 2 часа от текущего времени");
        }
    }
}
