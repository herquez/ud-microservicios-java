package com.galen.report_ms.services;

import com.galen.report_ms.helpers.ReportHelper;
import com.galen.report_ms.repositories.CompaniesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        return "";
    }

    @Override
    public void deleteReport(String reportName) {

    }
}
