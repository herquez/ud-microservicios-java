package com.galen.report_ms.services;

public interface ReportService {
    String makeReport(String reportName);
    String saveReport(String reportName);
    void deleteReport(String reportName);
}
