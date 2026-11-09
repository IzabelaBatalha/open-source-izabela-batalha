package com.workflex.workation.mapper;

import com.workflex.workation.domain.Workation;
import com.workflex.workation.dto.WorkationResponse;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class WorkationMapper {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public WorkationResponse toResponse(Workation workation) {
        return new WorkationResponse(
                workation.getWorkationId(),
                workation.getEmployee(),
                workation.getOrigin(),
                workation.getDestination(),
                workation.getStartDate().format(DATE_FORMAT),
                workation.getEndDate().format(DATE_FORMAT),
                workation.getWorkingDays(),
                workation.getRisk()
        );
    }
}
