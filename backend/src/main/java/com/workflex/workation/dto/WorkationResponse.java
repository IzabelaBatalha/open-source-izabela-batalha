package com.workflex.workation.dto;

import com.workflex.workation.domain.RiskLevel;

public record WorkationResponse(
        String workationId,
        String employee,
        String origin,
        String destination,
        String start,
        String end,
        int workingDays,
        RiskLevel risk
) {
}
