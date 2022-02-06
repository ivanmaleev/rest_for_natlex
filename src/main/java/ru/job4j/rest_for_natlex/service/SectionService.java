package ru.job4j.rest_for_natlex.service;

import ru.job4j.rest_for_natlex.model.Section;

import java.util.List;
import java.util.Optional;

public interface SectionService {
    List<Section> findAll();

    Section save(Section section);

    Optional<Section> findById(Long id);

    List<Section> findByGeologicalClassCode(String code);

    void delete(Section section);
}

