package ru.job4j.rest_for_natlex.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@Table(name = "geological_class")
@Accessors(chain = true)
public class GeologicalClass {
    @Id
    private String code;
    private String name;
}
