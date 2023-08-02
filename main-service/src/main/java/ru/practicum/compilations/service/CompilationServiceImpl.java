package ru.practicum.compilations.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationMapper;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.exception.CompilationNotFoundException;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements  CompilationService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    @Transactional
    public Compilation saveCompilation(NewCompilationDto dto) {
        Compilation compilation = CompilationMapper.getCompilationFromDto(dto);
        List<Event> events = new ArrayList<>();
        if (dto.getEvents() != null) {
            events = eventRepository.findAllById(dto.getEvents());
        }
        compilation.setEvents(events);
        return compilationRepository.save(compilation);
    }

    @Override
    public Compilation getCompilation(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new CompilationNotFoundException(String.format("Подборка с ID = %d не найдена", compId)));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        getCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public Compilation updateCompilation(Long compId, UpdateCompilationRequest request) {
        Compilation compilation = getCompilation(compId);
        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }
        if (request.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(request.getEvents());
            compilation.setEvents(events);
        }
        return compilationRepository.save(compilation);
    }

    @Override
    public List<Compilation> getCompilations(Boolean pinned, int from, int size) {
        int page = from / size;
        int pageLast = from % size;
        if (pageLast > 0) {
            page++;
        }
        if (pinned == null) {
            return compilationRepository.findAll(PageRequest.of(page, size)).toList();
        }
        return compilationRepository.findAllWithPinned(pinned, PageRequest.of(page, size));
    }
}
