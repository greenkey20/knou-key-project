package org.knou.keyproject.domain.chatgpt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.global.audit.BaseTimeEntity;

// 2023.8.23(수) 17h5
@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatGptResponseLine extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatGptResponseLineId;

    @Column(columnDefinition = "LONGTEXT")
    private String chatGptResponseLineString;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAN_ID")
    private Plan plan;

    private Boolean isDone;

    public void changeIsDone(Boolean isDone) {
        this.isDone = isDone;
    }



}
