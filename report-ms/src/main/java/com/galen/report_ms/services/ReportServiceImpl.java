package com.galen.report_ms.services;

import com.galen.report_ms.helpers.ReportHelper;
import com.galen.report_ms.models.Company;
import com.galen.report_ms.models.WebSite;
import com.galen.report_ms.repositories.CompaniesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Service
@AllArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final CompaniesRepository companiesRepository;
    private final ReportHelper reportHelper;

    @Override
    public String makeReport(String reportName) {
        return reportHelper.readTemplate(this.companiesRepository.getByName(reportName).orElseThrow());
    }

    @Override
    public String saveReport(String report) {
        var format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var placeholders = reportHelper.getPlaceholdersFromTemplate(report);
        var websites = Arrays.stream(placeholders.get(3).split(","))
                .map(String::trim)
                .filter(webSite -> !webSite.isEmpty())
                .map(webSite -> WebSite.builder().name(webSite).build())
                .toList();
        var company = Company.builder()
                .name(placeholders.get(0))
                .foundationDate(LocalDate.parse(placeholders.get(1), format))
                .founder(placeholders.get(2))
                .webSites(websites)
                .build();

        this.companiesRepository.postCompany(company);
        return makeReport(company.getName());
    }

    @Override
    public void deleteReport(String reportName) {

    }
}
