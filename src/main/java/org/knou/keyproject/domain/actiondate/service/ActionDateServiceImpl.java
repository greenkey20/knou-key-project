package org.knou.keyproject.domain.actiondate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.repository.ActionDateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ActionDateServiceImpl implements ActionDateService {
    private final ActionDateRepository actionDateRepository;

    // 2023.7.29(토) 22h35
    @Override
    @Transactional
    public void deleteActionDatesByPlanId(Long planId) {
        actionDateRepository.deleteAllByPlanPlanId(planId);
    }
}
