package com.galen.report_ms.services;

import com.galen.report_ms.helpers.ReportHelper;
import com.galen.report_ms.models.Company;
import com.galen.report_ms.repositories.CompaniesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
    public String saveReport(String reportName) {
        var company = Company.builder()
                .name(reportName)
                .logo("logo.jpg")
                .founder("founder")
                .foundationDate(LocalDate.now())
                .webSites(List.of())
                .build();
        this.companiesRepository.postCompany(company);
        return makeReport(company.getName());
    }

    @Override
    public void deleteReport(String reportName) {

    }
}
