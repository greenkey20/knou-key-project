package org.knou.keyproject.domain.action.entity;

import jakarta.persistence.*;
import lombok.*;
import org.knou.keyproject.domain.plan.entity.Plan;

// 2023.7.22(토) 0h30
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAN_ID")
    private Plan plan;

    private Long actionQuantity;
    private String actionContent;
    private String note;
    private String imageUrl;
}
