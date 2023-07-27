package org.knou.keyproject.domain.actiondate.entity;//package org.knou.keyproject.domain.action.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.knou.keyproject.domain.plan.entity.Plan;
//import org.knou.keyproject.global.audit.BaseTimeEntity;
//
//// 2023.7.22(토) 0h30 -> 2023.7.26(수) 15h ActionDate에 이 Action 클래스 내용 합침
//@Builder
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Entity
//public class Action extends BaseTimeEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long actionId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "PLAN_ID")
//    private Plan plan;
//
//    private Long actionQuantity;
//    private String actionContent;
//    private String note;
//    private String imageUrl;
//}
