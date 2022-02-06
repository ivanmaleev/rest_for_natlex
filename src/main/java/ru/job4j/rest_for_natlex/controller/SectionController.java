package ru.job4j.rest_for_natlex.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.rest_for_natlex.model.Section;
import ru.job4j.rest_for_natlex.service.SectionService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/sections")
@AllArgsConstructor
public class SectionController {

    private SectionService sectionService;

    @GetMapping
    public Iterable<Section> findAll() {
        return sectionService.findAll();
    }

    @GetMapping("/")
    public ResponseEntity<Section> findById(@RequestParam Long id) {
        Optional<Section> section = sectionService.findById(id);
        return new ResponseEntity<>(
                section.orElse(new Section()),
                section.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/by-code")
    public ResponseEntity<Iterable<Section>> findByGeologocalClassCode(@RequestParam String code) {
        List<Section> byGeologicalClassCode = sectionService.findByGeologicalClassCode(code);
        if (byGeologicalClassCode.size() == 0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(byGeologicalClassCode, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Section> save(@RequestBody Section section) {
        section = sectionService.save(section);
        return new ResponseEntity<>(
                section,
                HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Section section) {
        sectionService.save(section);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name) {
        Section section = new Section();
        section.setName(name);
        sectionService.delete(section);
        return ResponseEntity.ok().build();
    }
}
