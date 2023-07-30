package org.knou.keyproject.domain.actiondate.service;

import org.knou.keyproject.domain.actiondate.entity.ActionDate;

// 2023.7.29(토) 22h35
public interface ActionDateService {
    void deleteActionDatesByPlanId(Long planId);

    ActionDate findByActionDateId(Long actionDateId);
}
