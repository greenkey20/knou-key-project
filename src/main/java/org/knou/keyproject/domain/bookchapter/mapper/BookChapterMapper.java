package org.knou.keyproject.domain.bookchapter.mapper;

import org.knou.keyproject.domain.bookchapter.dto.BookChapterResponseDto;
import org.knou.keyproject.domain.bookchapter.entity.BookChapter;

import java.util.List;

public interface BookChapterMapper {
    BookChapterResponseDto toBookChapterResponseDto(BookChapter entity);

    List<BookChapterResponseDto> toBookChapterResponseDtoList(List<BookChapter> entities);
}
