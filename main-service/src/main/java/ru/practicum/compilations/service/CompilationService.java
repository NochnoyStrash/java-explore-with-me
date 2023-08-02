package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.model.Compilation;

import java.util.List;

public interface CompilationService {

    Compilation saveCompilation(NewCompilationDto dto);

    void deleteCompilation(Long compId);

    Compilation updateCompilation(Long compId, UpdateCompilationRequest request);

    Compilation getCompilation(Long compId);

    List<Compilation> getCompilations(Boolean pinned, int from, int size);
}
