package org.knou.keyproject.domain.bookchapter.serivce;

import org.knou.keyproject.domain.bookchapter.dto.BookChapterResponseDto;

import java.util.List;

public interface BookChapterService {
    List<BookChapterResponseDto> getTableOfContents(Long planId, String isbn13);
}
