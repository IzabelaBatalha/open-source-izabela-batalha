package com.workflex.workation.service;

import com.workflex.workation.config.CsvImportProperties;
import com.workflex.workation.domain.RiskLevel;
import com.workflex.workation.domain.Workation;
import com.workflex.workation.exception.CsvImportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkationCsvImporterTest {

    @Mock
    private WorkationService workationService;

    @Mock
    private CsvImportProperties csvImportProperties;

    @Mock
    private ResourceLoader resourceLoader;

    private WorkationCsvImporter csvImporter;

    @BeforeEach
    void setUp() {
        csvImporter = new WorkationCsvImporter(workationService, csvImportProperties, resourceLoader);
    }

    @Test
    void importFromCsv_parsesAllRows() {
        mockCsvResource("""
                workationId,employee,origin,destination,start,end,workingDays,risk
                w1,Steffen Jacobs,Germany,United States,2024-01-02,2024-12-31,65,HIGH
                w4,Andre Fischer,Germany,Greece,2023-05-22,2023-06-30,50,LOW
                w5,Ayushi Singh,Germany,India,2023-03-13,2023-04-30,35,NO
                """);

        csvImporter.importFromCsv("classpath:data/workations.csv");

        ArgumentCaptor<List<Workation>> captor = ArgumentCaptor.forClass(List.class);
        verify(workationService).saveAll(captor.capture());

        List<Workation> imported = captor.getValue();
        assertThat(imported).hasSize(3);
        assertThat(imported.getFirst().getRisk()).isEqualTo(RiskLevel.HIGH);
        assertThat(imported.get(1).getRisk()).isEqualTo(RiskLevel.LOW);
        assertThat(imported.get(2).getRisk()).isEqualTo(RiskLevel.NO);
    }

    @Test
    void importFromCsv_withRealClasspathFile_parsesProvidedDataset() {
        csvImporter = new WorkationCsvImporter(
                workationService,
                csvImportProperties,
                new DefaultResourceLoader());

        csvImporter.importFromCsv("classpath:data/workations.csv");

        ArgumentCaptor<List<Workation>> captor = ArgumentCaptor.forClass(List.class);
        verify(workationService).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(5);
    }

    @Test
    void importFromCsv_whenResourceMissing_throwsCsvImportException() {
        Resource missingResource = org.mockito.Mockito.mock(Resource.class);
        when(resourceLoader.getResource("missing.csv")).thenReturn(missingResource);
        when(missingResource.exists()).thenReturn(false);

        assertThatThrownBy(() -> csvImporter.importFromCsv("missing.csv"))
                .isInstanceOf(CsvImportException.class)
                .hasMessageContaining("CSV resource not found");
    }

    @Test
    void importFromCsv_whenFileEmpty_throwsCsvImportException() {
        mockCsvResource("");

        assertThatThrownBy(() -> csvImporter.importFromCsv("classpath:test/workations.csv"))
                .isInstanceOf(CsvImportException.class)
                .hasMessageContaining("CSV file is empty");
    }

    @Test
    void importFromCsv_whenRowHasInvalidColumnCount_throwsCsvImportException() {
        mockCsvResource("""
                workationId,employee,origin,destination,start,end,workingDays,risk
                w1,Steffen Jacobs,Germany
                """);

        assertThatThrownBy(() -> csvImporter.importFromCsv("classpath:test/workations.csv"))
                .isInstanceOf(CsvImportException.class)
                .hasMessageContaining("expected 8 columns");
    }

    @Test
    void importFromCsv_whenRowHasInvalidDate_throwsCsvImportException() {
        mockCsvResource("""
                workationId,employee,origin,destination,start,end,workingDays,risk
                w1,Steffen Jacobs,Germany,United States,not-a-date,2024-12-31,65,HIGH
                """);

        assertThatThrownBy(() -> csvImporter.importFromCsv("classpath:test/workations.csv"))
                .isInstanceOf(CsvImportException.class)
                .hasMessageContaining("Invalid CSV row at line 2");
    }

    @Test
    void importOnStartupIfEmpty_skipsWhenImportDisabled() {
        when(csvImportProperties.isEnabled()).thenReturn(false);

        csvImporter.importOnStartupIfEmpty();

        verify(workationService, never()).count();
        verify(workationService, never()).saveAll(org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    void importOnStartupIfEmpty_skipsWhenDataExists() {
        when(csvImportProperties.isEnabled()).thenReturn(true);
        when(workationService.count()).thenReturn(5L);

        csvImporter.importOnStartupIfEmpty();

        verify(workationService).count();
        verify(workationService, never()).saveAll(org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    void importOnStartupIfEmpty_importsWhenDatabaseIsEmpty() {
        when(csvImportProperties.isEnabled()).thenReturn(true);
        when(workationService.count()).thenReturn(0L);
        when(csvImportProperties.getResourcePath()).thenReturn("classpath:data/workations.csv");

        csvImporter = new WorkationCsvImporter(
                workationService,
                csvImportProperties,
                new DefaultResourceLoader());

        csvImporter.importOnStartupIfEmpty();

        verify(workationService).saveAll(org.mockito.ArgumentMatchers.anyList());
    }

    private void mockCsvResource(String content) {
        Resource resource = new ByteArrayResource(content.getBytes(StandardCharsets.UTF_8));
        when(resourceLoader.getResource(org.mockito.ArgumentMatchers.anyString())).thenReturn(resource);
    }
}
