package org.knou.keyproject.domain.chatgpt.repository;

import org.knou.keyproject.domain.chatgpt.entity.ChatGptResponseLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatGptResponseLineRepository extends JpaRepository<ChatGptResponseLine, Long> {

    List<ChatGptResponseLine> findAllByPlanPlanId(Long planId);
}
