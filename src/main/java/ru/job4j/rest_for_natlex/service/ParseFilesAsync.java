package ru.job4j.rest_for_natlex.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.job4j.rest_for_natlex.model.Job;
import ru.job4j.rest_for_natlex.model.JobStatus;
import ru.job4j.rest_for_natlex.model.Section;
import ru.job4j.rest_for_natlex.repository.JobRepository;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class ParseFilesAsync {

    private JobRepository jobRepository;
    private ParseService parseService;
    private SectionService sectionService;

    @Async
    public void storeFileAsync(Job job, int sheetNumber) throws IOException {

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        List<Section> sections = parseService.parseExcelToSections(job, sheetNumber);
        if (sections != null) {
            sections.forEach(s -> sectionService.save(s));
            job.setStatus(JobStatus.DONE);
        }
        jobRepository.save(job);
    }

    @Async
    public void exportSectionsToFile(Job job, List<Section> sections) {

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        parseService.exportSectionsToFile(job, sections);
        jobRepository.save(job);
    }

}
