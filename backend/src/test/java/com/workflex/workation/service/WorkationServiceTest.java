package com.workflex.workation.service;

import com.workflex.workation.domain.RiskLevel;
import com.workflex.workation.domain.Workation;
import com.workflex.workation.domain.WorkationSortField;
import com.workflex.workation.dto.WorkationResponse;
import com.workflex.workation.exception.InvalidSortPropertyException;
import com.workflex.workation.mapper.WorkationMapper;
import com.workflex.workation.repository.WorkationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkationServiceTest {

    @Mock
    private WorkationRepository workationRepository;

    @Mock
    private WorkationMapper workationMapper;

    @InjectMocks
    private WorkationService workationService;

    private Workation workation;
    private WorkationResponse response;

    @BeforeEach
    void setUp() {
        workation = Workation.builder()
                .workationId("w1")
                .employee("Steffen Jacobs")
                .origin("Germany")
                .destination("United States")
                .startDate(LocalDate.of(2024, 1, 2))
                .endDate(LocalDate.of(2024, 12, 31))
                .workingDays(65)
                .risk(RiskLevel.HIGH)
                .build();

        response = new WorkationResponse(
                "w1",
                "Steffen Jacobs",
                "Germany",
                "United States",
                "02/01/2024",
                "31/12/2024",
                65,
                RiskLevel.HIGH
        );
    }

    @Test
    void findAll_returnsMappedWorkations() {
        when(workationRepository.findAll(any(Sort.class))).thenReturn(List.of(workation));
        when(workationMapper.toResponse(workation)).thenReturn(response);

        List<WorkationResponse> result = workationService.findAll("employee", Sort.Direction.ASC);

        assertThat(result).containsExactly(response);
        verify(workationRepository).findAll(Sort.by(Sort.Direction.ASC, WorkationSortField.EMPLOYEE.getEntityField()));
    }

    @Test
    void findAll_withNullSort_usesDefaultEmployeeSort() {
        when(workationRepository.findAll(any(Sort.class))).thenReturn(List.of());

        workationService.findAll(null, Sort.Direction.DESC);

        verify(workationRepository).findAll(Sort.by(Sort.Direction.DESC, WorkationSortField.EMPLOYEE.getEntityField()));
    }

    @Test
    void findAll_withBlankSort_usesDefaultEmployeeSort() {
        when(workationRepository.findAll(any(Sort.class))).thenReturn(List.of());

        workationService.findAll("   ", Sort.Direction.ASC);

        verify(workationRepository).findAll(Sort.by(Sort.Direction.ASC, WorkationSortField.EMPLOYEE.getEntityField()));
    }

    @ParameterizedTest
    @CsvSource({
            "employee, employee",
            "origin, origin",
            "destination, destination",
            "start, startDate",
            "end, endDate",
            "workingDays, workingDays",
            "risk, risk"
    })
    void findAll_withValidSortColumns_mapsToEntityFields(String sortBy, String entityField) {
        when(workationRepository.findAll(any(Sort.class))).thenReturn(List.of());

        workationService.findAll(sortBy, Sort.Direction.DESC);

        verify(workationRepository).findAll(Sort.by(Sort.Direction.DESC, entityField));
    }

    @Test
    void findAll_withInvalidSort_throwsException() {
        assertThatThrownBy(() -> workationService.findAll("unknown", Sort.Direction.ASC))
                .isInstanceOf(InvalidSortPropertyException.class)
                .hasMessageContaining("unknown");
    }

    @Test
    void saveAll_delegatesToRepository() {
        List<Workation> workations = List.of(workation);

        workationService.saveAll(workations);

        verify(workationRepository).saveAll(workations);
    }

    @Test
    void count_delegatesToRepository() {
        when(workationRepository.count()).thenReturn(5L);

        assertThat(workationService.count()).isEqualTo(5L);
    }
}
