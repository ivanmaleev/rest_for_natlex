package ru.job4j.rest_for_natlex.service;

import ru.job4j.rest_for_natlex.model.Job;
import ru.job4j.rest_for_natlex.model.Section;

import java.util.List;

public interface ParseService {

    public List<Section> parseExcelToSections(Job job, int sheetNumber);

    public void exportSectionsToFile(Job job, List<Section> sections);
}
