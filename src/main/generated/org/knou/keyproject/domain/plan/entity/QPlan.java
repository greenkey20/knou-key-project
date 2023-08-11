package org.knou.keyproject.domain.plan.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlan is a Querydsl query type for Plan
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlan extends EntityPathBase<Plan> {

    private static final long serialVersionUID = 558001536L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlan plan = new QPlan("plan");

    public final org.knou.keyproject.global.audit.QBaseTimeEntity _super = new org.knou.keyproject.global.audit.QBaseTimeEntity(this);

    public final ListPath<org.knou.keyproject.domain.actiondate.entity.ActionDate, org.knou.keyproject.domain.actiondate.entity.QActionDate> actionDatesList = this.<org.knou.keyproject.domain.actiondate.entity.ActionDate, org.knou.keyproject.domain.actiondate.entity.QActionDate>createList("actionDatesList", org.knou.keyproject.domain.actiondate.entity.ActionDate.class, org.knou.keyproject.domain.actiondate.entity.QActionDate.class, PathInits.DIRECT2);

    public final ListPath<org.knou.keyproject.domain.board.entity.Board, org.knou.keyproject.domain.board.entity.QBoard> actionList = this.<org.knou.keyproject.domain.board.entity.Board, org.knou.keyproject.domain.board.entity.QBoard>createList("actionList", org.knou.keyproject.domain.board.entity.Board.class, org.knou.keyproject.domain.board.entity.QBoard.class, PathInits.DIRECT2);

    public final StringPath chatGptResponse = createString("chatGptResponse");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> deadlineDate = createDate("deadlineDate", java.time.LocalDate.class);

    public final StringPath deadlinePeriod = createString("deadlinePeriod");

    public final NumberPath<Integer> deadlinePeriodNum = createNumber("deadlinePeriodNum", Integer.class);

    public final StringPath deadlinePeriodUnit = createString("deadlinePeriodUnit");

    public final EnumPath<DeadlineType> deadlineType = createEnum("deadlineType", DeadlineType.class);

    public final StringPath frequencyDetail = createString("frequencyDetail");

    public final NumberPath<Double> frequencyFactor = createNumber("frequencyFactor", Double.class);

    public final EnumPath<FrequencyType> frequencyType = createEnum("frequencyType", FrequencyType.class);

    public final BooleanPath hasDeadline = createBoolean("hasDeadline");

    public final BooleanPath hasStartDate = createBoolean("hasStartDate");

    public final BooleanPath isChild = createBoolean("isChild");

    public final BooleanPath isMeasurable = createBoolean("isMeasurable");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final DatePath<java.time.LocalDate> lastStatusChangedAt = createDate("lastStatusChangedAt", java.time.LocalDate.class);

    public final org.knou.keyproject.domain.member.entity.QMember member;

    public final ListPath<Plan, QPlan> modifiedPlans = this.<Plan, QPlan>createList("modifiedPlans", Plan.class, QPlan.class, PathInits.DIRECT2);

    public final StringPath object = createString("object");

    public final QPlan parentPlan;

    public final NumberPath<Long> planId = createNumber("planId", Long.class);

    public final NumberPath<Integer> quantityPerDay = createNumber("quantityPerDay", Integer.class);

    public final NumberPath<Integer> quantityPerDayPredicted = createNumber("quantityPerDayPredicted", Integer.class);

    public final ListPath<org.knou.keyproject.domain.scrap.entity.Scrap, org.knou.keyproject.domain.scrap.entity.QScrap> scrapList = this.<org.knou.keyproject.domain.scrap.entity.Scrap, org.knou.keyproject.domain.scrap.entity.QScrap>createList("scrapList", org.knou.keyproject.domain.scrap.entity.Scrap.class, org.knou.keyproject.domain.scrap.entity.QScrap.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final EnumPath<PlanStatus> status = createEnum("status", PlanStatus.class);

    public final NumberPath<Integer> totalDurationDays = createNumber("totalDurationDays", Integer.class);

    public final NumberPath<Integer> totalNumOfActions = createNumber("totalNumOfActions", Integer.class);

    public final NumberPath<Integer> totalQuantity = createNumber("totalQuantity", Integer.class);

    public final StringPath unit = createString("unit");

    public QPlan(String variable) {
        this(Plan.class, forVariable(variable), INITS);
    }

    public QPlan(Path<? extends Plan> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlan(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlan(PathMetadata metadata, PathInits inits) {
        this(Plan.class, metadata, inits);
    }

    public QPlan(Class<? extends Plan> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.knou.keyproject.domain.member.entity.QMember(forProperty("member")) : null;
        this.parentPlan = inits.isInitialized("parentPlan") ? new QPlan(forProperty("parentPlan"), inits.get("parentPlan")) : null;
    }

}

