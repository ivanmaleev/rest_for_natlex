package ru.job4j.rest_for_natlex.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.rest_for_natlex.model.Job;

import java.io.IOException;

public interface FilesStorageService {

    public Job storeFile(MultipartFile file) throws IOException;

    public Job exportFile();

    public Resource loadFileAsResourceByJobId(Long id) throws Exception;
}
