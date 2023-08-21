package org.knou.keyproject.domain.bookchapter.mapper;

import org.knou.keyproject.domain.bookchapter.dto.BookChapterResponseDto;
import org.knou.keyproject.domain.bookchapter.entity.BookChapter;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookChapterMapper {
    BookChapterResponseDto toBookChapterResponseDto(BookChapter entity);

    List<BookChapterResponseDto> toBookChapterResponseDtoList(List<BookChapter> entities);
}
