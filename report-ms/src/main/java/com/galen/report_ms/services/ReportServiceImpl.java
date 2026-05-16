package com.galen.report_ms.services;

import com.galen.report_ms.repositories.CompaniesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final CompaniesRepository companiesRepository;

    @Override
    public String makeReport(String reportName) {
        return this.companiesRepository.getByName(reportName).orElseThrow().getName();
    }

    @Override
    public String saveReport(String reportName) {
        return "";
    }

    @Override
    public void deleteReport(String reportName) {

    }
}
