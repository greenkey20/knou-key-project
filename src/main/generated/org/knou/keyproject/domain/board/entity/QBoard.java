package org.knou.keyproject.domain.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 1942700308L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final org.knou.keyproject.global.audit.QBaseTimeEntity _super = new org.knou.keyproject.global.audit.QBaseTimeEntity(this);

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    public final EnumPath<BoardType> boardType = createEnum("boardType", BoardType.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final org.knou.keyproject.domain.member.entity.QMember member;

    public final org.knou.keyproject.domain.plan.entity.QPlan plan;

    public final NumberPath<Integer> readCount = createNumber("readCount", Integer.class);

    public final StringPath title = createString("title");

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.knou.keyproject.domain.member.entity.QMember(forProperty("member")) : null;
        this.plan = inits.isInitialized("plan") ? new org.knou.keyproject.domain.plan.entity.QPlan(forProperty("plan"), inits.get("plan")) : null;
    }

}

