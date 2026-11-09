package com.workflex.workation.controller;

import com.workflex.workation.domain.RiskLevel;
import com.workflex.workation.dto.WorkationResponse;
import com.workflex.workation.exception.GlobalExceptionHandler;
import com.workflex.workation.exception.InvalidSortPropertyException;
import com.workflex.workation.service.WorkationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkationController.class)
@Import(GlobalExceptionHandler.class)
class WorkationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkationService workationService;

    @Test
    void getWorkations_returnsOk() throws Exception {
        WorkationResponse workation = new WorkationResponse(
                "w1",
                "Steffen Jacobs",
                "Germany",
                "United States",
                "02/01/2024",
                "31/12/2024",
                65,
                RiskLevel.HIGH
        );

        when(workationService.findAll(isNull(), eq(Sort.Direction.ASC))).thenReturn(List.of(workation));

        mockMvc.perform(get("/workflex/workation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee").value("Steffen Jacobs"))
                .andExpect(jsonPath("$[0].risk").value("HIGH"));
    }

    @Test
    void getWorkations_withInvalidSort_returnsBadRequest() throws Exception {
        when(workationService.findAll(eq("invalid"), eq(Sort.Direction.ASC)))
                .thenThrow(new InvalidSortPropertyException("invalid", Set.of("employee")));

        mockMvc.perform(get("/workflex/workation").param("sort", "invalid"))
                .andExpect(status().isBadRequest());
    }
}
