package org.knou.keyproject.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -2106222430L;

    public static final QMember member = new QMember("member1");

    public final org.knou.keyproject.global.audit.QBaseTimeEntity _super = new org.knou.keyproject.global.audit.QBaseTimeEntity(this);

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final ListPath<org.knou.keyproject.domain.board.entity.Board, org.knou.keyproject.domain.board.entity.QBoard> boardList = this.<org.knou.keyproject.domain.board.entity.Board, org.knou.keyproject.domain.board.entity.QBoard>createList("boardList", org.knou.keyproject.domain.board.entity.Board.class, org.knou.keyproject.domain.board.entity.QBoard.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final BooleanPath isEmailVerified = createBoolean("isEmailVerified");

    public final DateTimePath<java.time.LocalDateTime> lastLoginAt = createDateTime("lastLoginAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final EnumPath<MemberPlatform> memberPlatform = createEnum("memberPlatform", MemberPlatform.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<org.knou.keyproject.domain.plan.entity.Plan, org.knou.keyproject.domain.plan.entity.QPlan> planList = this.<org.knou.keyproject.domain.plan.entity.Plan, org.knou.keyproject.domain.plan.entity.QPlan>createList("planList", org.knou.keyproject.domain.plan.entity.Plan.class, org.knou.keyproject.domain.plan.entity.QPlan.class, PathInits.DIRECT2);

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final ListPath<org.knou.keyproject.domain.scrap.entity.Scrap, org.knou.keyproject.domain.scrap.entity.QScrap> scrapList = this.<org.knou.keyproject.domain.scrap.entity.Scrap, org.knou.keyproject.domain.scrap.entity.QScrap>createList("scrapList", org.knou.keyproject.domain.scrap.entity.Scrap.class, org.knou.keyproject.domain.scrap.entity.QScrap.class, PathInits.DIRECT2);

    public final EnumPath<MemberStatus> status = createEnum("status", MemberStatus.class);

    public final NumberPath<Integer> yearOfBirth = createNumber("yearOfBirth", Integer.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

