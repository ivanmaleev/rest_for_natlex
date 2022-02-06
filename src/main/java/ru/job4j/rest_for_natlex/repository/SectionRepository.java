package ru.job4j.rest_for_natlex.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.rest_for_natlex.model.GeologicalClass;
import ru.job4j.rest_for_natlex.model.Section;

@Repository
public interface SectionRepository extends CrudRepository<Section, Long> {
    Iterable<Section> findAllByGeologicalClasses(GeologicalClass geoclass);
}
