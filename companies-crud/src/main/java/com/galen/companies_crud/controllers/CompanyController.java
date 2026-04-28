package com.galen.companies_crud.controllers;

import com.galen.companies_crud.entities.Company;
import com.galen.companies_crud.services.CompanyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping(path = "company")
@Slf4j
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping(path = "{name}")
    public ResponseEntity<Company> get(@PathVariable String name) {
        log.info("Getting Company with name {}", name);
        return ResponseEntity.ok(this.companyService.readByName(name));
    }

    @PostMapping
    public ResponseEntity<Company> post(@RequestBody Company company) {
        log.info("Creating Company with name {}", company.getName());
        return ResponseEntity.created(URI.create(this.companyService.create(company).getName())).build();
    }

    @PutMapping(path = "{name}")
    public ResponseEntity<Company> put(@RequestBody Company company, @PathVariable String name) {
        log.info("Updating Company with name {}", name);
        return ResponseEntity.ok(this.companyService.update(company, name));
    }

    @DeleteMapping(path = "{name}")
    public ResponseEntity<Company> delete(@PathVariable String name) {
        log.info("Deleting Company with name {}", name);
        this.companyService.delete(name);
        return ResponseEntity.noContent().build();
    }
}
