package ru.job4j.rest_for_natlex.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "section")
public class Section {
    @Id
    private String name;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<GeologicalClass> geologicalClasses = new ArrayList<>();

    public static int findMaxSizeOfGeologicalList(List<Section> sections) {
        int size = 0;
        for (Section section : sections) {
            if (section.getGeologicalClasses().size() > size) {
                size = section.getGeologicalClasses().size();
            }
        }
        return size;
    }

    public void addGeologicalClass(GeologicalClass gs) {
        if (gs != null && !"".equals(gs.getCode()) && gs.getCode() != null
                && !"".equals(gs.getName()) && gs.getName() != null) {
            geologicalClasses.add(gs);
        }
    }
}
