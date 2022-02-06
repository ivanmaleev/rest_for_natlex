package ru.job4j.rest_for_natlex.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.rest_for_natlex.model.Job;
import ru.job4j.rest_for_natlex.repository.JobRepository;
import ru.job4j.rest_for_natlex.service.JobService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class JobServiceImpl implements JobService {

    private JobRepository jobRepository;

    @Override
    public Job save(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }
}
