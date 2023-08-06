//package org.knou.keyproject.domain.board.repository;
//
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.JPQLQuery;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.knou.keyproject.domain.board.entity.Board;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//import static org.knou.keyproject.domain.board.entity.QBoard.board;
//
//@Slf4j
//@RequiredArgsConstructor
//@Repository
//public class BoardCustomRepositoryImpl implements BoardCustomRepository {
//    private final JPAQueryFactory jpaQueryFactory;
//
//    @Override
//    public Page<Board> findByTitleOrContentKeyword(String keyword, Pageable pageable) {
////        return jpaQueryFactory
////                .selectFrom(board)
////                .where(board.title.contains(keyword).or(board.content.contains(keyword).or(board.plan.object.contains(keyword))))
////                .orderBy(board.boardId.desc())
////                .fetch();
//    }
//}
