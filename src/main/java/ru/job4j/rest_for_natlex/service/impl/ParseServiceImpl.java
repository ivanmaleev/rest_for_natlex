package ru.job4j.rest_for_natlex.service.impl;

import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import ru.job4j.rest_for_natlex.model.GeologicalClass;
import ru.job4j.rest_for_natlex.model.Job;
import ru.job4j.rest_for_natlex.model.JobStatus;
import ru.job4j.rest_for_natlex.model.Section;
import ru.job4j.rest_for_natlex.service.ParseService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ParseServiceImpl implements ParseService {

    @Override
    public List<Section> parseExcelToSections(Job job, int sheetNumber) {
        try {
            Path path = FilesStorageServiceImpl.fileStoragePath.resolve(job.getFileName()).toAbsolutePath();
            Workbook workbook = new HSSFWorkbook(new FileInputStream(path.toString()));
            Sheet sheet = workbook.getSheetAt(sheetNumber);

            boolean firstRow = true;
            String column = "";
            String text = "";
            Section section = new Section();
            GeologicalClass gs = new GeologicalClass();
            List<Section> sections = new ArrayList<>();
            for (Row row : sheet) {
                if (!firstRow) {
                    section = new Section();
                    gs = new GeologicalClass();
                    column = "SectionName";
                    for (Cell cell : row) {
                        switch (column) {
                            case "SectionName":
                                text = parseCell(cell);
                                if (!"".equals(text) && text != null) {
                                    section.setName(text);
                                }
                                column = "ClassName";
                                break;
                            case "ClassName":
                                text = parseCell(cell);
                                if (!"".equals(text) && text != null) {
                                    gs.setName(parseCell(cell));
                                }
                                column = "ClassCode";
                                break;
                            case "ClassCode":
                                text = parseCell(cell);
                                if (!"".equals(text) && text != null) {
                                    gs.setCode(parseCell(cell));
                                }
                                section.addGeologicalClass(gs);
                                gs = new GeologicalClass();
                                column = "ClassName";
                                break;
                            default:
                                break;
                        }
                    }
                    sections.add(section);
                }
                firstRow = false;
            }
            return sections;
        } catch (Exception e) {
            job.setStatus(JobStatus.ERROR);
        }
        return null;
    }

    @Override
    public void exportSectionsToFile(Job job, List<Section> sections) {
        try {
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sections");

            // Шапка
            Row row = sheet.createRow(0);
            final int maxSizeOfGeologicalList = Section.findMaxSizeOfGeologicalList(sections);
            Cell cell = row.createCell(0);
            cell.setCellValue("Section name");
            for (int i = 0; i < maxSizeOfGeologicalList; i++) {
                cell = row.createCell(i * 2 + 1);
                cell.setCellValue("Class " + (i + 1) + " name");
                cell = row.createCell(i * 2 + 2);
                cell.setCellValue("Class " + (i + 1) + " code");
            }

            // Данные
            int rowNumber = 1;
            for (Section section : sections) {
                row = sheet.createRow(rowNumber);
                cell = row.createCell(0);
                cell.setCellValue(section.getName());
                int columnNumber = 1;
                for (GeologicalClass gc : section.getGeologicalClasses()) {
                    cell = row.createCell(columnNumber++);
                    cell.setCellValue(gc.getName());
                    cell = row.createCell(columnNumber++);
                    cell.setCellValue(gc.getCode());
                }
                rowNumber++;
            }
            String fileName = UUID.randomUUID().toString() + ".xls";
            Path path = FilesStorageServiceImpl.fileStoragePath.resolve(fileName).toAbsolutePath();
            FileOutputStream output = new FileOutputStream(path.toFile());
            workbook.write(output);
            job.setStatus(JobStatus.DONE);
            job.setFileName(fileName);
        } catch (Exception e) {
            job.setStatus(JobStatus.ERROR);
        }
    }

    private String parseCell(Cell cell) throws IllegalArgumentException {
        CellType cellType = cell.getCellType();
        if (cellType.equals(CellType.STRING)) {
            return cell.getRichStringCellValue().getString();
        } else if (cellType.equals(CellType.BLANK)) {
            return "";
        } else {
            throw new IllegalArgumentException("Wrong type of cell");
        }
    }
}
