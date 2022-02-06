package ru.job4j.rest_for_natlex.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.rest_for_natlex.model.Job;
import ru.job4j.rest_for_natlex.model.JobStatus;
import ru.job4j.rest_for_natlex.service.FilesStorageService;
import ru.job4j.rest_for_natlex.service.JobService;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("import")
@AllArgsConstructor
public class ImportController {

    private FilesStorageService filesStorageService;
    private JobService jobService;

    @PostMapping
    public Long importFile(@RequestParam("file") MultipartFile multipartFile) {
        Job job = filesStorageService.storeFile(multipartFile);
        return job.getId();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobStatus> findSectionsByJobId(@PathVariable String id) {
        Optional<Job> jobOptional = jobService.findById(Long.parseLong(id));
        if (!jobOptional.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(jobOptional.get().getStatus(), HttpStatus.OK);
    }
}
