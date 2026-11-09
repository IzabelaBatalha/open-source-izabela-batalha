package com.workflex.workation.mapper;

import com.workflex.workation.domain.RiskLevel;
import com.workflex.workation.domain.Workation;
import com.workflex.workation.dto.WorkationResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class WorkationMapperTest {

    private final WorkationMapper mapper = new WorkationMapper();

    @Test
    void toResponse_mapsAllFieldsAndFormatsDates() {
        Workation workation = Workation.builder()
                .workationId("w4")
                .employee("Andre Fischer")
                .origin("Germany")
                .destination("Greece")
                .startDate(LocalDate.of(2023, 5, 22))
                .endDate(LocalDate.of(2023, 6, 30))
                .workingDays(50)
                .risk(RiskLevel.LOW)
                .build();

        WorkationResponse response = mapper.toResponse(workation);

        assertThat(response.workationId()).isEqualTo("w4");
        assertThat(response.employee()).isEqualTo("Andre Fischer");
        assertThat(response.origin()).isEqualTo("Germany");
        assertThat(response.destination()).isEqualTo("Greece");
        assertThat(response.start()).isEqualTo("22/05/2023");
        assertThat(response.end()).isEqualTo("30/06/2023");
        assertThat(response.workingDays()).isEqualTo(50);
        assertThat(response.risk()).isEqualTo(RiskLevel.LOW);
    }
}
