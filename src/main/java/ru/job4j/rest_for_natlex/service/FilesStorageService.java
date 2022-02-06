package ru.job4j.rest_for_natlex.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.rest_for_natlex.model.Job;

public interface FilesStorageService {

    public Job storeFile(MultipartFile file);

    public Job exportFile();

    public Resource loadFileAsResourceByJobId(Long id) throws Exception;
}
