package ru.job4j.rest_for_natlex.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.rest_for_natlex.model.Job;
import ru.job4j.rest_for_natlex.model.JobStatus;
import ru.job4j.rest_for_natlex.service.FilesStorageService;
import ru.job4j.rest_for_natlex.service.JobService;

import java.util.Optional;

@RestController
@RequestMapping("export")
@AllArgsConstructor
public class ExportController {

    private JobService jobService;
    private FilesStorageService filesStorageService;

    @GetMapping("/{id}")
    public ResponseEntity<JobStatus> findSectionsByJobId(@PathVariable long id) {
        Optional<Job> jobOptional = jobService.findById(id);
        if (!jobOptional.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(jobOptional.get().getStatus(), HttpStatus.OK);
    }

    @GetMapping
    public Long exportFile() {
        Job job = filesStorageService.exportFile();
        return job.getId();
    }

    @GetMapping(value = "/{id}/file", produces = "application/vnd.ms-excel")
    public ResponseEntity<Resource> exportFile(@PathVariable long id) {
        try {
            Resource resource = filesStorageService.loadFileAsResourceByJobId(id);
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
