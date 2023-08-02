package ru.practicum.compilations.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.dto.EventMapper;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class CompilationMapper {

    public static Compilation getCompilationFromDto(NewCompilationDto dto) {
        List<Event> events = new ArrayList<>();
        boolean pinned = false;
        if (dto.getPinned() != null) {
            pinned = dto.getPinned();
        }
        return Compilation.builder()
                .pinned(pinned)
                .title(dto.getTitle())
                .events(events)
                .build();
    }

    public static CompilationDto getCompilationDto(Compilation compilation) {
        List<EventShortDto> events = compilation.getEvents().stream().map(EventMapper::shortEventDto).collect(Collectors.toList());
         return CompilationDto.builder()
                 .id(compilation.getId())
                 .pinned(compilation.isPinned())
                 .title(compilation.getTitle())
                 .events(events)
                 .build();
    }
}
