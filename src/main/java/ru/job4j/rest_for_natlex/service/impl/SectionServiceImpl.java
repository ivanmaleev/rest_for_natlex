package ru.job4j.rest_for_natlex.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.rest_for_natlex.model.GeologicalClass;
import ru.job4j.rest_for_natlex.model.Section;
import ru.job4j.rest_for_natlex.repository.SectionRepository;
import ru.job4j.rest_for_natlex.service.SectionService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SectionServiceImpl implements SectionService {

    private SectionRepository sectionRepository;

    @Override
    public void delete(Section section) {
        sectionRepository.delete(section);
    }

    @Override
    public List<Section> findAll() {
        return (List<Section>) sectionRepository.findAll();
    }

    @Override
    public Section save(Section section) {
        return sectionRepository.save(section);
    }

    @Override
    public Optional<Section> findById(Long id) {
        return sectionRepository.findById(id);
    }

    @Override
    public List<Section> findByGeologicalClassCode(String code) {
        GeologicalClass geoclass = new GeologicalClass().setCode(code);
        return (List) sectionRepository.findAllByGeologicalClasses(geoclass);
    }
}
