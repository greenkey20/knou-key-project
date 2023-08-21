package org.knou.keyproject.domain.bookchapter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.global.audit.BaseTimeEntity;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookChapter extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookChapterId;

    private String bookChapterString;

    @ManyToOne(fetch = FetchType.LAZY/*, cascade = CascadeType.ALL*/)
    @JoinColumn(name = "PLAN_ID")
    private Plan plan;

    private Boolean isDone;
    private String isbn13;

    public void changeIsDone(Boolean isDone) {
        this.isDone = isDone;
    }
}
