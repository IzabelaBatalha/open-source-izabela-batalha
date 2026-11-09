package com.workflex.workation.service;

import com.workflex.workation.config.CsvImportProperties;
import com.workflex.workation.domain.RiskLevel;
import com.workflex.workation.domain.Workation;
import com.workflex.workation.exception.CsvImportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class WorkationCsvImporter {

    private static final Logger log = LoggerFactory.getLogger(WorkationCsvImporter.class);

    private final WorkationService workationService;
    private final CsvImportProperties csvImportProperties;
    private final ResourceLoader resourceLoader;

    public WorkationCsvImporter(
            WorkationService workationService,
            CsvImportProperties csvImportProperties,
            ResourceLoader resourceLoader) {
        this.workationService = workationService;
        this.csvImportProperties = csvImportProperties;
        this.resourceLoader = resourceLoader;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void importOnStartupIfEmpty() {
        if (!csvImportProperties.isEnabled()) {
            log.info("CSV import disabled");
            return;
        }

        if (workationService.count() > 0) {
            log.info("Workations already present, skipping CSV import");
            return;
        }

        importFromCsv(csvImportProperties.getResourcePath());
    }

    public void importFromCsv(String resourcePath) {
        Resource resource = resourceLoader.getResource(resourcePath);
        if (!resource.exists()) {
            throw new CsvImportException("CSV resource not found: " + resourcePath);
        }

        List<Workation> workations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new CsvImportException("CSV file is empty");
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                workations.add(parseLine(line, lineNumber));
            }
        } catch (IOException ex) {
            throw new CsvImportException("Failed to read CSV file", ex);
        }

        workationService.saveAll(workations);
        log.info("Imported {} workations from CSV", workations.size());
    }

    private Workation parseLine(String line, int lineNumber) {
        String[] values = line.split(",", -1);
        if (values.length != 8) {
            throw new CsvImportException(
                    "Invalid CSV row at line " + lineNumber + ": expected 8 columns, found " + values.length);
        }

        try {
            return Workation.builder()
                    .workationId(values[0].trim())
                    .employee(values[1].trim())
                    .origin(values[2].trim())
                    .destination(values[3].trim())
                    .startDate(LocalDate.parse(values[4].trim()))
                    .endDate(LocalDate.parse(values[5].trim()))
                    .workingDays(Integer.parseInt(values[6].trim()))
                    .risk(RiskLevel.valueOf(values[7].trim()))
                    .build();
        } catch (IllegalArgumentException | DateTimeParseException ex) {
            throw new CsvImportException("Invalid CSV row at line " + lineNumber + ": " + ex.getMessage(), ex);
        }
    }
}
