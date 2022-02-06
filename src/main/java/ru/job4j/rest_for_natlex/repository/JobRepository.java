package ru.job4j.rest_for_natlex.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.rest_for_natlex.model.Job;

public interface JobRepository extends CrudRepository<Job, Long> {
}
