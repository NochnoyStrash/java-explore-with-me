package ru.practicum.compilations;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationMapper;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(@RequestBody @Valid NewCompilationDto dto) {
        Compilation compilation = compilationService.saveCompilation(dto);
        return CompilationMapper.getCompilationDto(compilation);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @NotNull long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable @NotNull Long compId, @RequestBody @Valid UpdateCompilationRequest request) {
        Compilation compilation = compilationService.updateCompilation(compId, request);
        return CompilationMapper.getCompilationDto(compilation);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable @NotNull Long compId) {
        Compilation compilation = compilationService.getCompilation(compId);
        return CompilationMapper.getCompilationDto(compilation);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return  compilationService.getCompilations(pinned, from, size).stream()
                .map(CompilationMapper::getCompilationDto).collect(Collectors.toList());
    }
}
