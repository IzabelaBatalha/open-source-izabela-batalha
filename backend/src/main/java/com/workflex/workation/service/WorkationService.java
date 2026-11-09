package com.workflex.workation.service;

import com.workflex.workation.domain.Workation;
import com.workflex.workation.domain.WorkationSortField;
import com.workflex.workation.dto.WorkationResponse;
import com.workflex.workation.mapper.WorkationMapper;
import com.workflex.workation.repository.WorkationRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WorkationService {

    private final WorkationRepository workationRepository;
    private final WorkationMapper workationMapper;

    public WorkationService(WorkationRepository workationRepository, WorkationMapper workationMapper) {
        this.workationRepository = workationRepository;
        this.workationMapper = workationMapper;
    }

    public List<WorkationResponse> findAll(String sortBy, Sort.Direction direction) {
        Sort sort = WorkationSortField.fromRequestParam(sortBy).toSort(direction);
        return workationRepository.findAll(sort).stream()
                .map(workationMapper::toResponse)
                .toList();
    }

    @Transactional
    public void saveAll(List<Workation> workations) {
        workationRepository.saveAll(workations);
    }

    public long count() {
        return workationRepository.count();
    }
}
