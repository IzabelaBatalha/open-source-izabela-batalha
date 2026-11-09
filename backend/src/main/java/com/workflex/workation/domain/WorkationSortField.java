package com.workflex.workation.domain;

import com.workflex.workation.exception.InvalidSortPropertyException;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public enum WorkationSortField {

    EMPLOYEE("employee", "employee"),
    ORIGIN("origin", "origin"),
    DESTINATION("destination", "destination"),
    START("start", "startDate"),
    END("end", "endDate"),
    WORKING_DAYS("workingdays", "workingDays"),
    RISK("risk", "risk");

    public static final WorkationSortField DEFAULT = EMPLOYEE;

    private final String requestParam;
    private final String entityField;

    WorkationSortField(String requestParam, String entityField) {
        this.requestParam = requestParam;
        this.entityField = entityField;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public String getEntityField() {
        return entityField;
    }

    public static WorkationSortField fromRequestParam(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return DEFAULT;
        }

        String normalized = sortBy.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(field -> field.requestParam.equals(normalized))
                .findFirst()
                .orElseThrow(() -> new InvalidSortPropertyException(sortBy, allowedRequestParams()));
    }

    public static Set<String> allowedRequestParams() {
        return Arrays.stream(values())
                .map(WorkationSortField::getRequestParam)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Sort toSort(Sort.Direction direction) {
        Sort.Direction resolvedDirection = direction != null ? direction : Sort.Direction.ASC;
        return Sort.by(resolvedDirection, entityField);
    }
}
