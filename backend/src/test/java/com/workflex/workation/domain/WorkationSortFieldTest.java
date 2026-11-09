package com.workflex.workation.domain;

import com.workflex.workation.exception.InvalidSortPropertyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkationSortFieldTest {

    @Test
    void fromRequestParam_withNullOrBlank_returnsDefault() {
        assertThat(WorkationSortField.fromRequestParam(null)).isEqualTo(WorkationSortField.DEFAULT);
        assertThat(WorkationSortField.fromRequestParam("  ")).isEqualTo(WorkationSortField.DEFAULT);
    }

    @ParameterizedTest
    @CsvSource({
            "employee, EMPLOYEE",
            "origin, ORIGIN",
            "destination, DESTINATION",
            "start, START",
            "end, END",
            "workingDays, WORKING_DAYS",
            "risk, RISK"
    })
    void fromRequestParam_withValidValues_returnsMatchingField(String requestParam, WorkationSortField expected) {
        assertThat(WorkationSortField.fromRequestParam(requestParam)).isEqualTo(expected);
    }

    @Test
    void fromRequestParam_withInvalidValue_throwsInvalidSortPropertyException() {
        assertThatThrownBy(() -> WorkationSortField.fromRequestParam("invalid"))
                .isInstanceOf(InvalidSortPropertyException.class)
                .hasMessageContaining("invalid");
    }

    @Test
    void toSort_mapsToEntityFieldWithDirection() {
        Sort sort = WorkationSortField.START.toSort(Sort.Direction.DESC);

        assertThat(sort.getOrderFor("startDate").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void allowedRequestParams_containsAllSortColumns() {
        assertThat(WorkationSortField.allowedRequestParams())
                .containsExactlyInAnyOrder(
                        "employee", "origin", "destination", "start", "end", "workingdays", "risk");
    }
}
