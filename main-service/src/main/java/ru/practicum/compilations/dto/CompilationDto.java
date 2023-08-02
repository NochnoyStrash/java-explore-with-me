package ru.practicum.compilations.dto;

import lombok.*;
import ru.practicum.events.dto.EventShortDto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    @NotNull
    @Size(min = 1, max = 50, message = "Заголовок должен быть длинной от 1 до 50 символов")
    private String title;
}
