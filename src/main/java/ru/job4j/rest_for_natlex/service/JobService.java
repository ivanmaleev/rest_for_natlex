package ru.job4j.rest_for_natlex.service;

import ru.job4j.rest_for_natlex.model.Job;

import java.util.Optional;

public interface JobService {

    public Job save(Job job);

    public Optional<Job> findById(Long id);
}
