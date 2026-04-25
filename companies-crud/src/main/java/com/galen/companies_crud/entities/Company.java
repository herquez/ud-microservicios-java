package com.galen.companies_crud.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String founder;
    private String logo;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fundationDate;
}
