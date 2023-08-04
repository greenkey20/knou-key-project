package org.knou.keyproject.domain.actiondate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QActionDate is a Querydsl query type for ActionDate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QActionDate extends EntityPathBase<ActionDate> {

    private static final long serialVersionUID = 18459222L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QActionDate actionDate = new QActionDate("actionDate");

    public final org.knou.keyproject.global.audit.QBaseTimeEntity _super = new org.knou.keyproject.global.audit.QBaseTimeEntity(this);

    public final NumberPath<Long> actionDateId = createNumber("actionDateId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath dateFormat = createString("dateFormat");

    public final EnumPath<DateType> dateType = createEnum("dateType", DateType.class);

    public final BooleanPath isDone = createBoolean("isDone");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath memo = createString("memo");

    public final StringPath numOfDate = createString("numOfDate");

    public final NumberPath<Integer> numOfDay = createNumber("numOfDay", Integer.class);

    public final NumberPath<Integer> numOfMonth = createNumber("numOfMonth", Integer.class);

    public final StringPath numOfYear = createString("numOfYear");

    public final org.knou.keyproject.domain.plan.entity.QPlan plan;

    public final NumberPath<Integer> planActionQuantity = createNumber("planActionQuantity", Integer.class);

    public final DatePath<java.time.LocalDate> realActionDate = createDate("realActionDate", java.time.LocalDate.class);

    public final NumberPath<Integer> realActionQuantity = createNumber("realActionQuantity", Integer.class);

    public final NumberPath<Integer> reviewScore = createNumber("reviewScore", Integer.class);

    public final StringPath schedule = createString("schedule");

    public final NumberPath<Integer> timeTakenForRealAction = createNumber("timeTakenForRealAction", Integer.class);

    public QActionDate(String variable) {
        this(ActionDate.class, forVariable(variable), INITS);
    }

    public QActionDate(Path<? extends ActionDate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QActionDate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QActionDate(PathMetadata metadata, PathInits inits) {
        this(ActionDate.class, metadata, inits);
    }

    public QActionDate(Class<? extends ActionDate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plan = inits.isInitialized("plan") ? new org.knou.keyproject.domain.plan.entity.QPlan(forProperty("plan"), inits.get("plan")) : null;
    }

}

