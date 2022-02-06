package ru.job4j.rest_for_natlex.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.rest_for_natlex.model.Job;
import ru.job4j.rest_for_natlex.model.JobStatus;
import ru.job4j.rest_for_natlex.repository.JobRepository;
import ru.job4j.rest_for_natlex.service.ParseFilesAsync;
import ru.job4j.rest_for_natlex.service.FilesStorageService;
import ru.job4j.rest_for_natlex.service.SectionService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ParseFilesAsync parseFilesAsync;
    @Autowired
    private SectionService sectionService;
    @Value("${filesstorageserviceimpl.filesstoragepath}")
    private String FILES_STORAGE_PATH;
    public static Path fileStoragePath;
    @Value("${filesstorageserviceimpl.sheetnumber}")
    private int SHEET_NUMBER = 0;

    @PostConstruct
    public void init() throws IOException {
        this.fileStoragePath = Paths.get(FILES_STORAGE_PATH)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStoragePath);
        } catch (Exception ex) {
            throw new IOException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public Job exportFile() {
        Job job = new Job().setStatus(JobStatus.IN_PROGRESS);
        job = jobRepository.save(job);
        parseFilesAsync.exportSectionsToFile(job, sectionService.findAll());
        return job;
    }

    @Override
    public Job storeFile(MultipartFile file) {
        Job job = new Job().setStatus(JobStatus.IN_PROGRESS);
        job = jobRepository.save(job);
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "." + StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                job.setStatus(JobStatus.ERROR);
                job = jobRepository.save(job);
            } else {
                Path targetLocation = this.fileStoragePath.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                job.setFileName(fileName);
                job = jobRepository.save(job);
                parseFilesAsync.storeFileAsync(job, SHEET_NUMBER);
            }
        } catch (IOException ex) {
            job.setStatus(JobStatus.ERROR);
            job = jobRepository.save(job);
        }
        return job;
    }

    @Override
    public Resource loadFileAsResourceByJobId(Long id) throws Exception {
        Optional<Job> job = jobRepository.findById(id);
        if (!job.isPresent()) {
            throw new Exception("Job not found");
        }
        if(job.get().getStatus() != JobStatus.DONE){
            throw new Exception("File not ready yet");
        }
        try {
            return loadFileAsResource(job.get().getFileName());
        } catch (IOException e) {
            throw new Exception("File not found");
        }
    }

    public Resource loadFileAsResource(String fileName) throws IOException {
        try {
            Path filePath = this.fileStoragePath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new IOException("File not found " + fileName);
            }
        } catch (Exception ex) {
            throw new IOException("File not found " + fileName, ex);
        }
    }
}
