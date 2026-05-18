package com.galen.report_ms.helpers;

import com.galen.report_ms.models.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportHelper {

    @Value("${report.template}")
    private String reportTemplate;

    public String readTemplate(Company company) {
        System.out.printf(this.reportTemplate);
        return this.reportTemplate
                .replace("{company}", company.getName())
                .replace("{foundation_date}", company.getFoundationDate().toString())
                .replace("{founder}", company.getFounder());
    }
}
