package org.knou.keyproject.domain.bookchapter.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.bookchapter.dto.BookChapterResponseDto;
import org.knou.keyproject.domain.bookchapter.entity.BookChapter;
import org.knou.keyproject.domain.bookchapter.mapper.BookChapterMapper;
import org.knou.keyproject.domain.bookchapter.repository.BookChapterRepository;
import org.knou.keyproject.domain.plan.dto.BooksListSearchResponseDto;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.service.PlanService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@PropertySource(value = "classpath:application-local.yml")
@Transactional(readOnly = true)
@Service
public class BookChapterServiceImpl implements BookChapterService {
    private final BookChapterRepository bookChapterRepository;
    private final BookChapterMapper bookChapterMapper;
    private final PlanService planService;

    @Value("${aladin.api.key}")
    private String bookApiKey;

    @Override
    public List<BookChapterResponseDto> getTableOfContents(Long planId, String isbn) {
        saveBookChapter(planId, isbn);

        List<BookChapter> bookChapterList = bookChapterRepository.findAllByPlanPlanIdOrderByBookChapterIdAsc(planId);

        return bookChapterMapper.toBookChapterResponseDtoList(bookChapterList);
    }

    private void saveBookChapter(Long planId, String isbn) {
        String itemRequestUrl = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=" + bookApiKey + "&itemIdType=ISBN13&ItemId=" + isbn + "&output=js&Version=20131101&OptResult=Toc";
        //http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttbgreenkey201608001 &itemIdType=ISBN13 &output=js &Version=20131101 &ItemId=9791189909284 &Version=20131101 &OptResult=Toc
        RestTemplate restTemplate = new RestTemplate();
        BooksListSearchResponseDto responseDto = restTemplate.getForObject(itemRequestUrl, BooksListSearchResponseDto.class);

        if (!responseDto.getItem().isEmpty()) {
            String tableOfContents = responseDto.getItem().get(0).getSubInfo().getToc();
            log.info("검색 책 목차 = " + tableOfContents);

            String[] bookChapterStrings = tableOfContents.split("<BR>");
            log.info("검색 책 목차 파싱 결과 = " + Arrays.toString(bookChapterStrings));

            for (String bookChapterString : bookChapterStrings) {
                if (bookChapterString != null || bookChapterString != "") {
                    Plan findPlan = planService.findVerifiedPlan(planId);

                    BookChapter bookChapter = BookChapter.builder()
                            .bookChapterString(bookChapterString)
                            .plan(findPlan)
                            .isDone(false)
                            .isbn13(isbn)
                            .build();

                    bookChapterRepository.save(bookChapter);
                }
            }
        }
    }
}