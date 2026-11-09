package com.workflex.workation.controller;

import com.workflex.workation.dto.WorkationResponse;
import com.workflex.workation.service.WorkationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workflex/workation")
@Tag(name = "Workations", description = "Operations for workation records")
public class WorkationController {

    private final WorkationService workationService;

    public WorkationController(WorkationService workationService) {
        this.workationService = workationService;
    }

    @GetMapping
    @Operation(
            summary = "List workations",
            description = "Returns all workations from the database. Supports sorting by a single column at a time.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workations retrieved successfully",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkationResponse.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid sort parameter")
            }
    )
    public ResponseEntity<List<WorkationResponse>> getWorkations(
            @Parameter(description = "Sort column: employee, origin, destination, start, end, workingDays, risk")
            @RequestParam(required = false) String sort,
            @Parameter(description = "Sort direction: asc or desc")
            @RequestParam(required = false, defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromOptionalString(direction)
                .orElse(Sort.Direction.ASC);

        List<WorkationResponse> workations = workationService.findAll(sort, sortDirection);
        return ResponseEntity.ok(workations);
    }
}
