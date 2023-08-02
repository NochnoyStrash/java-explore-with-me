package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.events.location.LocationDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {
    @NotEmpty
    @Size(max = 2000, min = 20, message = "Длинна сообщения должна быть в пределах от 20 до 2000 символов.")
    private String annotation;
    @NotNull
    private Long category;
    @NotEmpty
    @Size(max = 7000, min = 20, message = "Длинна сообщения должна быть в пределах от 20 до 7000 символов.")
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    @NotEmpty
    @Size(max = 120, min = 3, message = "Длинна сообщения должна быть в пределах от 3 до 120 символов.")
    private String title;
}
