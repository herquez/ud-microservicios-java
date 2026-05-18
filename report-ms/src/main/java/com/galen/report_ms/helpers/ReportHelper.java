package com.galen.report_ms.helpers;

import com.galen.report_ms.models.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class ReportHelper {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([a-zA-Z_]+)}");
    private static final Set<String> COMPANY_PLACEHOLDERS = Set.of("company", "foundation_date", "founder", "logo", "web_sites");

    @Value("${report.template}")
    private String reportTemplate;

    public String readTemplate(Company company) {
        return this.reportTemplate
                .replace("{company}", company.getName())
                .replace("{foundation_date}", company.getFoundationDate().toString())
                .replace("{founder}", company.getFounder())
                .replace("{web_sites}", company.getWebSites().toString());
    }

    public List<String> getPlaceholdersFromTemplate(String template) {
        var split = template.split("\\{");
        return Arrays.stream(split)
                .filter(line -> !line.isEmpty())
                .map(line -> {
                    var end = line.indexOf("}");
                    return line.substring(0, end);
                })
                .collect(Collectors.toList());
    }

}
