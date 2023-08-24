package org.knou.keyproject.domain.plan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.actiondate.mapper.ActionDateMapper;
import org.knou.keyproject.domain.actiondate.repository.ActionDateRepository;
import org.knou.keyproject.domain.actiondate.service.ActionDateService;
import org.knou.keyproject.domain.board.entity.Board;
import org.knou.keyproject.domain.board.repository.BoardRepository;
import org.knou.keyproject.domain.chatgpt.dto.ChatGptRequestDto;
import org.knou.keyproject.domain.chatgpt.dto.ChatGptResponseDto;
import org.knou.keyproject.domain.chatgpt.entity.ChatGptResponseLine;
import org.knou.keyproject.domain.chatgpt.repository.ChatGptResponseLineRepository;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.repository.MemberRepository;
import org.knou.keyproject.domain.member.service.MemberService;
import org.knou.keyproject.domain.plan.dto.*;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.knou.keyproject.domain.plan.mapper.PlanMapper;
import org.knou.keyproject.domain.plan.repository.PlanRepository;
import org.knou.keyproject.global.utils.Calendar;
import org.knou.keyproject.global.utils.PlanStatisticUtils;
import org.knou.keyproject.global.utils.calculator.Calculator;
import org.knou.keyproject.global.utils.paging.PageInfo;
import org.knou.keyproject.global.utils.paging.Pagination;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.knou.keyproject.global.utils.StringParsingUtils.replaceNewLineWithBr;
import static org.knou.keyproject.global.utils.StringParsingUtils.splitIntoLinesByBr;

// 2023.7.23(일) 22h
@Slf4j
@RequiredArgsConstructor
@PropertySource(value = "classpath:application-local.yml")
@Transactional(readOnly = true)
@Service
public class PlanServiceImpl implements PlanService {
    private final BoardRepository boardRepository;
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final PlanMapper planMapper;
    private final ActionDateRepository actionDateRepository;
    private final MemberService memberService;
    private final ActionDateService actionDateService;
    private final Calendar calendar;
    private final Calculator calculator;
    private final PlanStatisticUtils planStatisticUtils;
    private final ActionDateMapper actionDateMapper;
    private final ChatGptResponseLineRepository chatGptResponseLineRepository;

    // 2023.8.2(수) 1h50 ChatGpt 호출 관련 추가
    @Qualifier("openaiRestTemplate")
    private final RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${aladin.api.key}")
    private String bookApiKey;

    private final int PAGE_LIMIT = 5; // 1페이지당 보여질 페이징 바 수
    private final int BOARD_LIMIT = 15; // 1페이지당 보여질 게시판 리스트/게시글 수

    @Override
    @Transactional
    public Plan saveNewPlan(PlanPostRequestDto requestDto) {
        Plan planToCalculate = planMapper.toEntity(requestDto);

        if (requestDto.getMemberId() != null) {
            planToCalculate.setMember(memberRepository.findById(requestDto.getMemberId()).orElse(null));
        }

        Plan calculatedPlan = calculator.calculateNewPlan(planToCalculate);
        log.info("계산 결과 시작일 = " + calculatedPlan.getStartDate()); // 2023.7.29(토) 4h25 '계산 결과 시작일 = 2023-07-29' 찍힘
        log.info("계산 결과 활동일 중 첫번째 것 = " + calculatedPlan.getActionDatesList().get(0).getDateType().toString()); // 2023.7.29(토) 4h25 actionDatesList()가 비었다고 한다..
//        System.out.println("이거 안 찍히나?" + calculatedPlan.getActionDatesList().get(0).getDateType().toString()); // 2023.7.28(금) 3h15 이거 안 찍히나?ACTION 찍히는데

        // 2023.8.21(월) 0h50
        List<ActionDate> actionDatesList = setOrdersAndContents(calculatedPlan);

        // 2023.7.29(토) 4h20
        actionDateRepository.saveAll(actionDatesList);

        return planRepository.save(calculatedPlan);
    }

    @Override
    @Transactional
    public List<ActionDate> setOrdersAndContents(Plan calculatedPlan) {
        List<ActionDate> actionDatesList = calculatedPlan.getActionDatesList(); // 시작일~종료일까지 순차적으로 생성한 바, actionDateId 오름차순으로 정렬되어있다고 볼 수 있음
        int quantityPerDay = calculatedPlan.getQuantityPerDay();

        for (int i = 0; i < actionDatesList.size(); i++) {
            ActionDate thisActionDate = actionDatesList.get(i);

            int order = i + 1;
            int startUnit = quantityPerDay * i + 1;
            int endUnit;

            if (i < actionDatesList.size() - 1) {
                endUnit = order * quantityPerDay;
            } else {
                endUnit = startUnit + thisActionDate.getPlanActionQuantity() - 1;
            }

            thisActionDate.setOrders(order);
            thisActionDate.setPlanStartUnit(startUnit);
            thisActionDate.setPlanEndUnit(endUnit);
        }

        return actionDatesList;
    }

    // 2023.7.24(월) 17h40 -> 22h40 startDate 입력에 따른 계산 결과 반영
    @Override
    @Transactional
    public void saveMyNewPlan(MyPlanPostRequestDto requestDto) {
        Plan findPlan = findVerifiedPlan(requestDto.getPlanId());
//        List<ActionDate> actionDatesBeforeNewCal = actionDateRepository.findByPlanPlanId(findPlan.getPlanId());

        Member findMember = null;
        // 로그인 안 하고 저장을 희망하는 이용자가 있을 수 있어서, 아래 null 처리 필요
        if (requestDto.getMemberId() != null) {
            findMember = memberService.findVerifiedMember(requestDto.getMemberId());
        }

        findPlan.setMember(findMember);

        findPlan.setStatus(PlanStatus.ACTIVE);

        // 애초에 startDate 지정하지 않았던 계획의 경우에만 금번 requestDto에 startDate가 들어있음
        if (!findPlan.getHasStartDate()) {
            if (findPlan.getStartDate() != requestDto.getStartDate()) {
                findPlan.setStartDate(requestDto.getStartDate()); // 2023.8.5(토) 21h15 버그 발견 = 이 부분을 누락했었음
                // 2023.7.29(토) 21h30 '오늘'을 임의의 시작일로 계산했던 actionDatesList 삭제
//                actionDateRepository.deleteAll(findPlan.getActionDatesList()); // 2023.7.29(토) 22h 이렇게는 delete 쿼리가 안 나갔음
                actionDateService.deleteActionDatesByPlanId(findPlan.getPlanId());
                findPlan.setActionDatesList(null);
            }

            findPlan.setStartDate(requestDto.getStartDate());
        }

        log.info("planService에서 calculator 호출 전 findPlan.deadline date" + findPlan.getDeadlineDate());
        findPlan = calculator.calculateRealNewPlan(findPlan); // 2023.7.29(토) 1h25 나의 생각 = 여기서 actionDate들 다시 set됨 = 이게 insert가 됨 -> 나의 질문 = 이걸 update되게 하려면 어떻게 해야 하지? 현재 repository 거치지 않아서 그런가?
        log.info("planService에서 calculator 호출 후 findPlan.deadline date = " + findPlan.getDeadlineDate());
//        List<ActionDate> actionDatesAfterNewCal = findPlan.getActionDatesList();
//        actionDateRepository.saveAll(actionDatesAfterNewCal);
//        for (int i = 0; i < actionDatesBeforeNewCal.size(); i++) {
//            Long actionDateBeforeNewCalId = actionDatesBeforeNewCal.get(i).getActionDateId();
//
//            for (int j = 0; j < actionDatesAfterNewCal.size(); j++) {
//                Long actionDateAfterNewCalId = actionDatesAfterNewCal.get(j).getActionDateId();
//
//                if (actionDateBeforeNewCalId == actionDateAfterNewCalId) {
//                    actionDatesBeforeNewCal.get(i).setNumOfYear();
//                }
//            }
//        }

        // 2023.7.27(목) 2h35 회원 가입 - 로그인 - 계산 - 저장 - 목록 조회 테스트 하다 생각난 점 보완 = 처음 계산 시 deadline 지정 안 했어도, 위 과정에서 계산 결과에 따른 deadlineDate가 생기는 바, 이 날짜로 정보를 저장하자
        if (!findPlan.getHasDeadline()) {
            int year = Integer.parseInt(findPlan.getActionDatesList().get(findPlan.getActionDatesList().size() - 1).getNumOfYear());
            int month = findPlan.getActionDatesList().get(findPlan.getActionDatesList().size() - 1).getNumOfMonth();
            int date = Integer.parseInt(findPlan.getActionDatesList().get(findPlan.getActionDatesList().size() - 1).getNumOfDate());

            findPlan.setDeadlineDate(LocalDate.of(year, month, date));
        }

        // 2023.8.21(월) 1h20 수행 내용(from~to단위) 정보 포함하기 위해 추가
        List<ActionDate> actionDatesList = setOrdersAndContents(findPlan);
        actionDateRepository.saveAll(actionDatesList); // 2023.8.21(월) 1h25 나의 궁금증 = 이것도 필요 없으려나? JPA 어렵다

        // 2023.8.23(수) 16h50 추가 = 사용자가 ChatGPT 질의 결과를 '나의 일정'에 저장하기로 결심했다면, 그 때 ChatGPT response 문자열을 line별 파싱해서 별도의 테이블에 저장한다
        if (!findPlan.getIsMeasurable()) {
            String[] responseLines = splitIntoLinesByBr(findPlan.getChatGptResponse());
            log.info("ChatGPT 답변 파싱 결과 = " + Arrays.toString(responseLines));

            for (String responseLine : responseLines) {
                if (responseLine != null || responseLine != "" || responseLine.length() == 0) {
                    ChatGptResponseLine chatGptResponseLine = ChatGptResponseLine.builder()
                            .chatGptResponseLineString(responseLine)
                            .plan(findPlan)
                            .isDone(false)
                            .build();

                    chatGptResponseLineRepository.save(chatGptResponseLine);
                }
            }
        }

        // 2023.7.29(토) 0h15 jpa update를 어떻게 하는 건지 갑자기 정확히 알지/이해 못하는 것 같아서 googling -> https://study-easy-coding.tistory.com/143
        // setter로 값 변경하면 변경 감지해서 수정 쿼리 날려 db에 반영 = dirty checking
//        planRepository.save(findPlan);
        // 2023.7.29(토) 0h35 나의 궁금증 = 위 save() 호출 안 하는데, 왜 아직도 새로 저장되지..?
    } // saveMyNewPlan() 메서드 종료

    // 2023.7.29(토) 22h20 추가 = 계산 결과 저장을 위해 로그인 하고 왔을 때 actionDates가 추가로 저장(추가로 insert문들이 나가고 있었음)되지 않도록 하기 위해 = 이 경우에는 해당 memberId도 member만 변경해주면 됨
    @Override
    @Transactional
    public void saveMyNewPlanAfterLogin(MyPlanPostRequestDto requestDto) {
        Plan findPlan = findVerifiedPlan(requestDto.getPlanId());
        findPlan.setMember(memberService.findVerifiedMember(requestDto.getMemberId()));
    }

    @Override
    public Plan findVerifiedPlan(Long planId) {
        return planRepository.findById(planId).orElse(null);
    }

    // 2023.7.24(월) 17h20 자동 기본 구현만 해둠 -> 23h10 내용 구현
    @Override
    public List<MyPlanListResponseDto> findPlansByMember(Long memberId, int currentPage, int size) {
        Member findMember = memberRepository.findById(memberId).orElse(null);
        return planRepository.findAllByMemberMemberIdOrderByPlanIdDesc(findMember.getMemberId(), PageRequest.of(currentPage - 1, size, Sort.by("planId").descending()))
                .stream()
                .map(plan -> planMapper.toMyPlanListResponseDto(plan))
                .collect(Collectors.toList());
    }

    // 2023.7.28(금) 1h45 MapStruct 사용하여 수정
    @Override
    public Plan findPlanById(Long planId) {
        Plan findPlan = findVerifiedPlan(planId);


        // 2023.7.31(월) 4h 일정 상세보기 페이지에서 달력 출력하기 위해 추가(fetch lazy라서 이게 필요하다? JPA 잘 모르고 하려니까 힘들다 ㅠㅠ)
        List<ActionDate> findActionDates = actionDateRepository.findByPlanPlanId(planId);
        if (findActionDates != null) {
            findPlan.setActionDatesList(findActionDates);
        }

        return findPlan;
    }

    @Override
    public MyPlanDetailResponseDto getMyPlanDetailResponseDto(Long planId) {
        Plan findPlan = findVerifiedPlan(planId);
        List<ActionDate> actionDatesList = actionDateRepository.findByPlanPlanId(findPlan.getPlanId());

        MyPlanDetailResponseDto.MyPlanDetailResponseDtoBuilder myPlanDetailResponseDto = MyPlanDetailResponseDto.builder();
        myPlanDetailResponseDto.planId(findPlan.getPlanId());
        myPlanDetailResponseDto.isMeasurable(findPlan.getIsMeasurable());
        myPlanDetailResponseDto.object(findPlan.getObject());
        myPlanDetailResponseDto.totalQuantity(findPlan.getTotalQuantity());
        myPlanDetailResponseDto.unit(findPlan.getUnit());

        myPlanDetailResponseDto.hasStartDate(findPlan.getHasStartDate());
        myPlanDetailResponseDto.startDate(findPlan.getStartDate());
        myPlanDetailResponseDto.startYear(findPlan.getStartDate().getYear());
        myPlanDetailResponseDto.startMonth(findPlan.getStartDate().getMonthValue());

        if (findPlan.getHasDeadline()) {
            myPlanDetailResponseDto.deadlineDate(findPlan.getDeadlineDate());
            myPlanDetailResponseDto.deadlineYear(findPlan.getDeadlineDate().getYear());
            myPlanDetailResponseDto.deadlineMonth(findPlan.getDeadlineDate().getMonthValue());
        } else {
            ActionDate lastActionDate = actionDatesList.get(actionDatesList.size() - 1);
            LocalDate deadlineDate = LocalDate.of(Integer.valueOf(lastActionDate.getNumOfYear()), lastActionDate.getNumOfMonth(), Integer.parseInt(lastActionDate.getNumOfDate()));
            myPlanDetailResponseDto.deadlineDate(deadlineDate);
            myPlanDetailResponseDto.deadlineYear(deadlineDate.getYear());
            myPlanDetailResponseDto.deadlineMonth(deadlineDate.getMonthValue());
        }

        myPlanDetailResponseDto.hasDeadline(findPlan.getHasDeadline());

        myPlanDetailResponseDto.frequencyDetail(findPlan.getFrequencyDetail());
        myPlanDetailResponseDto.totalDurationDays(findPlan.getTotalDurationDays());
        myPlanDetailResponseDto.totalNumOfActions(findPlan.getTotalNumOfActions());
        myPlanDetailResponseDto.quantityPerDay(findPlan.getQuantityPerDay());
        myPlanDetailResponseDto.actionDatesList(actionDateMapper.entitiesToDtos(actionDatesList));
        myPlanDetailResponseDto.status(findPlan.getStatus());

        myPlanDetailResponseDto.lastStatusChangedAt(findPlan.getLastStatusChangedAt());
        myPlanDetailResponseDto.isChild(findPlan.getIsChild());

        // 2023.8.23(수) 22h 추가 = 일정 상세 조회 시 누군가의 child인 경우에는 부모의 목차 정보를 사용하기 위해서 추가
        if (findPlan.getIsChild()) {
            myPlanDetailResponseDto.parentPlanId(findPlan.getParentPlan().getPlanId());
        }

        myPlanDetailResponseDto.sizeOfModifiedPlansList(findPlan.getModifiedPlans().size());

        // 2023.8.21(월) 14h55
        if (findPlan.getIsBook()) {
            String isbn13 = findPlan.getIsbn13();
//            myPlanDetailResponseDto.isBook(findPlan.getIsBook());
            myPlanDetailResponseDto.isbn13(isbn13);

//            String tableOfContents = bookChapterService.getTableOfContents(isbn13);
//            myPlanDetailResponseDto.tableOfContents(tableOfContents);
        }

        return myPlanDetailResponseDto.build();
    }


    // 2023.8.7(월) 16h5
    @Override
    public List<MyPlanDetailResponseDto> getMyPlanDetailResponseDtos(Long memberId) {
        List<Plan> myPlans = findMyPlans(memberId);

        List<MyPlanDetailResponseDto> myPlanDetailResponseDtos = new ArrayList<>();

        for (Plan myPlan : myPlans) {
            Long planId = myPlan.getPlanId();
            MyPlanDetailResponseDto myPlanDetailResponseDto = getMyPlanDetailResponseDto(planId);
            myPlanDetailResponseDtos.add(myPlanDetailResponseDto);
        }

        return myPlanDetailResponseDtos;
    }

    // 2023.8.7(월) 17h40
    @Override
    public List<MyPlanListResponseDto> getMyPlanListResponseDtoList(List<Plan> list) {
        List<MyPlanListResponseDto> responseDtos = new ArrayList<>();

        for (Plan myPlan : list) {
            Long planId = myPlan.getPlanId();
            Plan findPlan = findVerifiedPlan(planId);

            MyPlanListResponseDto.MyPlanListResponseDtoBuilder responseDto = MyPlanListResponseDto.builder();
            responseDto.planId(planId);
            responseDto.isChild(findPlan.getIsChild());

            Member findMember = memberService.findVerifiedMember(findPlan.getMember().getMemberId());
            responseDto.nickname(findMember.getNickname());

            responseDto.object(findPlan.getObject());
            responseDto.isMeasurable(findPlan.getIsMeasurable());
            responseDto.status(findPlan.getStatus());
            responseDto.startDate(findPlan.getStartDate());
            responseDto.hasDeadline(findPlan.getHasDeadline());
            responseDto.deadlineDate(findPlan.getDeadlineDate());

            responseDto.totalDurationDays(findPlan.getTotalDurationDays());

            responseDto.totalNumOfActions(findPlan.getTotalNumOfActions());
            responseDto.totalQuantity(findPlan.getTotalQuantity());
            responseDto.frequencyDetail(findPlan.getFrequencyDetail());
            responseDto.quantityPerDay(findPlan.getQuantityPerDay());
            responseDto.unit(findPlan.getUnit());

            responseDto.sizeOfModifiedPlansList(findPlan.getModifiedPlans().size());

            responseDtos.add(responseDto.build());
        }

        return responseDtos;
    }

    private List<Plan> findMyPlans(Long memberId) {
        return planRepository.findMyPlans(memberId);
    }

    // 2023.8.4(금) 22h50 plan 상세보기 요청 controller에 대응하기 위한 메서드 + JSP에서 비즈니스 로직 최대한 빼기 위해 2023.8.5(토) 1H30 추가
    @Override
    public MyPlanStatisticDetailResponseDto getPlanStatisticDetailById(Long planId) {
        Plan findPlan = findVerifiedPlan(planId);
        LocalDate startDate = findPlan.getStartDate();
        LocalDate lastStatusChangedAt = findPlan.getLastStatusChangedAt();

        int accumulatedRealActionQuantity = planStatisticUtils.getAccumulatedRealActionQuantity(planId);
        int accumulatedPlanActionQuantity = planStatisticUtils.getAccumulatedPlanActionQuantity(planId, startDate);
        int quantityDifferenceBetweenPlanAndReal = planStatisticUtils.getQuantityDifferenceBetweenPlanAndReal(accumulatedPlanActionQuantity, accumulatedRealActionQuantity);

        int totalQuantity = findPlan.getTotalQuantity();
        int quantityToEndPlan = planStatisticUtils.getQuantityToEndPlan(totalQuantity, accumulatedRealActionQuantity);

        double ratioOfRealActionQuantityTillToday = planStatisticUtils.getRatioOfRealActionQuantityTillToday(accumulatedRealActionQuantity, totalQuantity);
        double ratioOfQuantityToEndPlan = planStatisticUtils.getRatioOfQuantityToEndPlan(accumulatedRealActionQuantity, totalQuantity);

        int accumulatedNumOfActions = planStatisticUtils.getAccumulatedNumOfActions(planId);
        int numOfActionsToEndPlan = planStatisticUtils.getNumOfActionsToEndPlan(findPlan.getTotalNumOfActions(), accumulatedNumOfActions);

        int accumulatedPlanActionQuantityBeforePause = planStatisticUtils.getAccumulatedPlanActionQuantityBeforePause(planId, startDate, lastStatusChangedAt);

        MyPlanStatisticDetailResponseDto result = MyPlanStatisticDetailResponseDto.builder()
                .accumulatedRealActionQuantity(accumulatedRealActionQuantity)
                .accumulatedPlanActionQuantity(accumulatedPlanActionQuantity)
                .quantityDifferenceBetweenPlanAndReal(quantityDifferenceBetweenPlanAndReal)
                .quantityToEndPlan(quantityToEndPlan)
                .ratioOfRealActionQuantityTillToday(planStatisticUtils.formatPercentage(ratioOfRealActionQuantityTillToday))
                .ratioOfQuantityToEndPlan(planStatisticUtils.formatPercentage(ratioOfQuantityToEndPlan))
                .accumulatedNumOfActions(planStatisticUtils.getAccumulatedNumOfActions(planId))
                .numOfActionsToEndPlan(numOfActionsToEndPlan)
                .averageTimeTakenForRealAction(planStatisticUtils.getAverageTimeTakenForRealAction(planId))
                .accumulatedPlanActionQuantityBeforePause(accumulatedPlanActionQuantityBeforePause)
                .build();

        if (findPlan.getLastStatusChangedAt() != null && findPlan.getStatus().equals(PlanStatus.PAUSE)) {
            result.setPeriodDaysBeforePause(planStatisticUtils.getPeriodDaysBeforePause(findPlan, startDate));
        }

        return result;
    }

    // 2023.7.31(월) 19h20
//    @Value("${aladin.requesturl}")
//    public static String listRequestUrl;

    @Override
    public Map<String, Object> searchBookTitle(String bookSearchKeyword, int currentPage) {
        // 알라딘 도서 검색 open API 호출 -> json data 결과 얻기 -> json data 결과 얻어 item에 해당하는 값들을 가져옴
        String listRequestUrl = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=" + bookApiKey + "&Query=" + bookSearchKeyword + "&QueryType=Keyword&MaxResults=" + BOARD_LIMIT + "&start=" + currentPage + "&SearchTarget=Book&output=js&Version=20131101"; // &MaxResults=20&start=1
        /* 2023.8.7(월) 페이지네이션에 대한 나의 생각
        1. 해당 keyword 검색 결과 총 개수가 몇 개인지 파악
        2. 나의 페이지네이션 변수들에 맞게 잘라서, 1페이지씩의 분량을 만듦
        3. view에서 전달해오는 페이지 숫자에 따라, 또는 '다음 페이지' 버튼이 눌리면, api에 다른/다음 페이지로 요청
         */

        RestTemplate restTemplate = new RestTemplate();
        BooksListSearchResponseDto responseDto = restTemplate.getForObject(listRequestUrl, BooksListSearchResponseDto.class, bookSearchKeyword);

        List<BooksListSearchResponseDto.Item> items = responseDto.getItem(); // currentPage부터 시작하는 15개 결과 받음

        Map<String, Object> results = new HashMap<>();

        PageInfo pageInfo = Pagination.getPageInfo(items.size(), currentPage, PAGE_LIMIT, BOARD_LIMIT);
        results.put("pageInfo", pageInfo);

        List<BookInfoDto> bookInfoDtos = new ArrayList<>();

        if (items.size() == 0) { // 조회 결과가 없는 경우
            results.put("bookInfoDtos", bookInfoDtos);
        } else {
            for (BooksListSearchResponseDto.Item item : items) {
                String isbn = item.getIsbn13();
                if (isbn.length() == 13) { // 2023.8.21(월) 14h5 나의 생각 = isbn13자리가 아닌/없는 책의 응답 처리는 어떻게 하는 것이 좋을까?
//                    Map<String, Object> additionalBookInfo = getAdditionalBookInfo(isbn);
                    Integer numOfPages = getNumOfPages(isbn);

                    BookInfoDto bookInfoDto = BookInfoDto.builder()
                            .title(item.getTitle())
                            .author(item.getAuthor())
                            .pubDate(item.getPubDate())
                            .description(item.getDescription())
                            .isbn13(item.getIsbn13())
                            .cover(item.getCover())
                            .publisher(item.getPublisher())
                            .link(item.getLink())
                            .numOfPages(numOfPages)
//                            .tableOfContents((String) additionalBookInfo.get("tableOfContents"))
                            .build();

//                log.info("이번에 담기는 item = " + bookInfoDto);
                    bookInfoDtos.add(bookInfoDto);
                }
            }

            results.put("bookInfoDtos", bookInfoDtos);
        }

        return results;
    }

    // 2023.8.20(일) 4h20 -> 6h API 구조 다시 보니 필요 없음 <- API 요청 시 시작 페이지와 maxResults를 지정하는데, 이걸 각각 currentPage와 boardLimit과 맞추면 될 듯
    private Map<String, Object> pageBookSearchResult(List<BooksListSearchResponseDto.Item> items, int currentPage) {
        PageInfo pageInfo = Pagination.getPageInfo(items.size(), currentPage, PAGE_LIMIT, BOARD_LIMIT);
        int endRow = pageInfo.getCurrentPage() * pageInfo.getBoardLimit();
        int startRow = endRow - pageInfo.getBoardLimit() + 1;

        List<BooksListSearchResponseDto.Item> thisPageItems = new ArrayList<>();

        for (int i = startRow; i <= endRow; i++) {
            BooksListSearchResponseDto.Item item = items.get(i);
            thisPageItems.add(item);
        }

        Map<String, Object> results = new HashMap<>();
        results.put("thisPageItems", thisPageItems);
        results.put("pageInfo", pageInfo);

        return results;
    }

    // 2023.8.2(수) 2h5
    @Override
    public String getChatGptResponse(PlanPostRequestDto requestDto) {
        String prompt = requestDto.getDeadlinePeriodNum() + requestDto.getDeadlinePeriodUnit() + " 동안 " + requestDto.getObject(); // ~기간 동안? ~기간 (이)내에?

        if (requestDto.getDeadlinePeriodUnit().equals("일") || requestDto.getDeadlinePeriodUnit().equals("주")) {
            prompt += " 위한 일간 계획을 세워줘";
        } else {
            prompt += " 위한 주간 계획을 세워줘";
        }

        ChatGptRequestDto chatGptRequestDto = new ChatGptRequestDto(model, prompt);
        ChatGptResponseDto chatGptResponseDto = restTemplate.postForObject(apiUrl, chatGptRequestDto, ChatGptResponseDto.class);
        if (chatGptResponseDto == null || chatGptResponseDto.getChoices() == null || chatGptResponseDto.getChoices().isEmpty()) {
            return "답변을 받지 못했습니다";
        }

        String content = chatGptResponseDto.getChoices().get(0).getMessage().getContent();

//        return parseChatGptResponse(content);
        return replaceNewLineWithBr(content); // 2023.8.23(수) 16h35 게시글 관련 이 메서드 만든 게 생각나 바꿔봄
    }

    // 2023.8.5(토) 0h25 컨트롤러에서 하던 역할을 여기로 분리
    @Override
    public List<List<ActionDate>> getActionDatesCalendars(Long planId) {
        Plan findPlan = findVerifiedPlan(planId);
        return calendar.getCalendars(findPlan);
    }

    // 2023.8.5(토) 0h25 컨트롤러에서 하던 역할을 여기로 분리
    @Override
    public List<List<ActionDate>> getPlanCalendars(Plan savedPlan) {
        return calendar.getCalendars(savedPlan);
    }

    // 2023.8.5(토) 0h25 컨트롤러에서 하던 역할을 여기로 분리
    @Override
    public List<ActionDate> getArrowCalendar(int year, int month) {
        return calendar.getArrowCalendar(year, month);
    }

    // 2023.8.24(목) 12h 추가
    @Override
    public List<ActionDate> getArrowCalendar(int year, int month, int date) {
        return calendar.getArrowCalendar(year, month, date);
    }

    @Override
    @Transactional
    public Long resumePlan(Long planId) {
        Plan planToResume = findVerifiedPlan(planId);
//        planToResume.setStatus(PlanStatus.ACTIVE); // 부모 plan의 상태를 다시 active로 바꿈 -> 새로 만들어지는 자식 plan의 상태도 active로 세팅됨 vs 2023.8.6(일) 4h55 나의 생각 = 부모 plan 상태는 pause로 두는 것이 나을 것 같다(그래야 활동 중인 목록이 제대로 나올 듯)
        planToResume.setLastStatusChangedAt(LocalDate.now()); // 기존 plan의 상태가 오늘 바뀐 바, 해당 내용 업데이트

        // 기존/부모 plan이 가지고 있던 actionDatesList 중 오늘 이후의 것들은 삭제하는 것이 맞을 것 같은데..
        /* 2023.8.5(토) 18h30 Plan 엔티티/도메인 영역으로 옮겨봄
        List<ActionDate> originalActionDatesList = actionDateRepository.findByPlanPlanId(planId);
        int lastIndex = originalActionDatesList.size() - 1;
        LocalDate lastActionDate = LocalDate.parse(originalActionDatesList.get(lastIndex).getDateFormat(), DateTimeFormatter.ISO_DATE);
        for (LocalDate date = LocalDate.now(); date.isBefore(lastActionDate); date.plusDays(1), lastIndex--) {
            actionDateRepository.delete(originalActionDatesList.get(lastIndex));
        }

        planToResume.setActionDatesList(originalActionDatesList); // 기존 plan의 actionDatesList로써 위와 같이 삭제하고 난 결과를 세팅함
         */

        deleteActionDatesAfterResumePlan(planId);
//        planToResume.setActionDatesList(resultList);

        // 기존/부모/수정 전 plan의 내용을 그대로 가지고 가도록 이렇게 대입 -> 2023.8.5(토) 17h15 나의 생각 = 이게 의도한대로 가능한지는 테스트 필요 -> 2023.8.6(일) 0h45 저장이 안 되는 것 같아서 '객체 복사' 검색해옴
        // 2023.8.23(수) 15h 수정(그동안 추가된 필드들 추가)
        Plan resumedPlan = Plan.builder()
                .member(planToResume.getMember())
                .isMeasurable(planToResume.getIsMeasurable())
                .object(planToResume.getObject())
                .totalQuantity(planToResume.getTotalQuantity())
                .unit(planToResume.getUnit())
                .hasStartDate(planToResume.getHasStartDate())
                .startDate(planToResume.getStartDate())
                .frequencyType(planToResume.getFrequencyType())
                .frequencyDetail(planToResume.getFrequencyDetail())
                .hasDeadline(planToResume.getHasDeadline())
                .deadlineType(planToResume.getDeadlineType())
                .deadlineDate(planToResume.getDeadlineDate())
                .deadlinePeriod(planToResume.getDeadlinePeriod())
                .deadlinePeriodNum(planToResume.getDeadlinePeriodNum())
                .deadlinePeriodUnit(planToResume.getDeadlinePeriodUnit())
                .quantityPerDayPredicted(planToResume.getQuantityPerDayPredicted())
                .chatGptResponse(planToResume.getChatGptResponse())
                .parentPlan(planToResume)
                .status(PlanStatus.ACTIVE)
                .lastStatusChangedAt(planToResume.getLastStatusChangedAt())
                .totalDurationDays(planToResume.getTotalDurationDays())
                .totalNumOfActions(planToResume.getTotalNumOfActions())
                .quantityPerDay(planToResume.getQuantityPerDay())
                .frequencyFactor(planToResume.getFrequencyFactor())
                .isChild(true)
                .isBook(planToResume.getIsBook())
                .isbn13(planToResume.getIsbn13())
                .tableOfContents(planToResume.getTableOfContents())
                .build();
//        resumedPlan = customBeanUtils.copyNonNullProperties(planToResume, resumedPlan);

        resumedPlan = calculator.calculateResumePlan(resumedPlan);
        resumedPlan = planRepository.save(resumedPlan);

        planToResume.addModifiedPlan(resumedPlan);

        return resumedPlan.getPlanId();
    }

    // 2023.8.5(토) 18h25 PlanServiceImpl에 resume plan 관련해서 작성했던 내용을 여기로 옮김 -> 2023.8.5(토) 21h20 테스트 실행 시 ActionDate 영속성 컨텍스트에 반영 안 됨(ActionDate 영속성 없음)

    /**
     * 잔여 기간 및 잔여 분량 고려해서 resume(재시작)하는 날부터 다시 계산하여 actionDates를 만들 것인 바, 기존에 있던 actionDates 자료는 삭제
     *
     * @param planId
     * @return
     */
    @Transactional
    public void deleteActionDatesAfterResumePlan(Long planId) {
        List<ActionDate> originalActionDatesList = actionDateRepository.findByPlanPlanId(planId);
        log.info("deleteActionDatesAfterResumePlan()에서의 originalActionDatesList의 길이 = " + originalActionDatesList.size());

        int lastIndex = originalActionDatesList.size() - 1;
        LocalDate lastActionDate = LocalDate.parse(originalActionDatesList.get(lastIndex).getDateFormat(), DateTimeFormatter.ISO_DATE);
        log.info("lastActionDate = " + lastActionDate);

        for (LocalDate date = lastActionDate; date.isAfter(LocalDate.now()) || date.equals(LocalDate.now()); date = date.minusDays(1)) {
            ActionDate thisActionDate = originalActionDatesList.get(lastIndex);
            LocalDate actionDate = LocalDate.parse(thisActionDate.getDateFormat(), DateTimeFormatter.ISO_DATE);

            if (!thisActionDate.getIsDone() && actionDate.equals(date)) {
//                originalActionDatesList.remove(originalActionDatesList.get(lastIndex));
                actionDateRepository.delete(originalActionDatesList.get(lastIndex));
                lastIndex--;

                if (lastIndex < 0) {
                    break;
                }
            }
            log.info("for문 돌 때 date = " + date + ", lastIndex = " + lastIndex);
        } // for문 영역 끝

//        return originalActionDatesList;
    }

    @Override
    @Transactional
    public void pausePlan(Long planId) {
        Plan planToPause = findVerifiedPlan(planId);
        planToPause.setStatus(PlanStatus.PAUSE);
        planToPause.setLastStatusChangedAt(LocalDate.now());
        planToPause.pauseActionDates();
    }

    // 2023.8.6(일) 5h50 나의 생각 = 포기한 경우 actionDates 안 지우고 놔두는 게 의미가 있을까? 추후 어떤 내용을 포기했는지 궁금해 할 수도 있으니, 놔둬도 괜찮을 것 같고..
    @Override
    @Transactional
    public void giveUpPlan(Long planId) {
        Plan planToGiveUp = findVerifiedPlan(planId);
        planToGiveUp.setStatus(PlanStatus.GIVEUP);
        planToGiveUp.setLastStatusChangedAt(LocalDate.now());
        planToGiveUp.giveUpActionDates();
    }

    @Override
    public Page<Plan> findAllByMemberMemberIdOrderByPlanIdDesc(Long memberId, Pageable pageable) {
        Member findMember = memberService.findVerifiedMember(memberId);
        return planRepository.findAllByMemberMemberIdOrderByPlanIdDesc(findMember.getMemberId(), pageable);
    }

    @Override
    public List<MyPlanStatisticDetailResponseDto> findStatisticDtosByMember(Long memberId) {
        Member findMember = memberService.findVerifiedMember(memberId);

        List<MyPlanStatisticDetailResponseDto> statisticDtos = new ArrayList<>();

        List<Plan> findPlansByMember = planRepository.findAllByMemberMemberIdOrderByPlanIdDesc(findMember.getMemberId());
        for (Plan plan : findPlansByMember) {
            Long thisPlanId = plan.getPlanId();
            MyPlanStatisticDetailResponseDto statisticDto = getPlanStatisticDetailById(thisPlanId);
            statisticDtos.add(statisticDto);
        }

        return statisticDtos;
    }

    // 2023.8.20(일) 20h45
    @Override
    public List<MainPageResponseDto> findMainPageInfoByMember(Long memberId) {
        Member findMember = memberService.findVerifiedMember(memberId);
        Long findMemberId = findMember.getMemberId();

        // 특정 회원이 진행 중(ACTIVE 상태)인 활동들의 기본 정보(planList) 및 몇 가지 통계 정보(statList) 필요
        List<MyPlanDetailResponseDto> planList = findAllActivePlansByMemberMemberId(findMemberId);  // planId 내림차순 정렬
        List<MyPlanStatisticDetailResponseDto> statList = findAllActiveStatisticDtosByMember(planList); // planList 리스트 순서대로 순회해서 만들어옴

        return combineIntoMainPageResponseDtoList(planList, statList);
    }

    // 2023.8.24(목) 0h 추가
    private List<MainPageResponseDto> combineIntoMainPageResponseDtoList(List<MyPlanDetailResponseDto> planList, List<MyPlanStatisticDetailResponseDto> statList) {
        // 이론적으로 planList 및 statList의 크기는 동일해야 함 <- 특정 회원 id를 기준으로 조회한 planList를 가지고 statList를 만든 것임
        log.info("planList 크기 == statList 크기? = " + (planList.size() == statList.size()));

        List<MainPageResponseDto> mainPageResponseDtos = new ArrayList<>();

        for (int i = 0; i < planList.size(); i++) {
            MyPlanDetailResponseDto thisPlan = planList.get(i);
            MyPlanStatisticDetailResponseDto thisStat = statList.get(i);

            MainPageResponseDto mainPageResponseDto = planMapper.toMainPageResponseDto(thisPlan, thisStat);
            mainPageResponseDtos.add(mainPageResponseDto);
        }

        return mainPageResponseDtos;
    }

    // 2023.8.23(수) 23h45
    @Override
    public List<MainPageResponseDto> findPlanListInfoByMember(Long memberId) {
        Member findMember = memberService.findVerifiedMember(memberId);
        Long findMemberId = findMember.getMemberId();

        List<MyPlanDetailResponseDto> planList = findAllPlansByMemberMemberId(findMemberId);
        List<MyPlanStatisticDetailResponseDto> statList = findAllStatisticDtosByMember(planList);
        log.info("planList 크기 == statList 크기? = " + (planList.size() == statList.size()));

        return combineIntoMainPageResponseDtoList(planList, statList);
    }

    // 2023.8.21(월) 17h40 추가
    @Override
    public MyPlanListResponseDto getPlanAboutBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElse(null);
        Plan findPlan = findBoard.getPlan();

        return planMapper.toMyPlanListResponseDto(findPlan);
    }

    // 2023.8.21(월) 17h40 추가
    @Override
    public MyPlanStatisticDetailResponseDto getPlanStatisticDetailAboutBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElse(null);
        Long planId = findBoard.getPlan().getPlanId();
        return getPlanStatisticDetailById(planId);
    }

    private List<MyPlanStatisticDetailResponseDto> findAllActiveStatisticDtosByMember(List<MyPlanDetailResponseDto> planList) {
        List<MyPlanStatisticDetailResponseDto> statisticDtos = new ArrayList<>();

        for (MyPlanDetailResponseDto plan : planList) {
            Long thisPlanId = plan.getPlanId();
            MyPlanStatisticDetailResponseDto statisticDto = getPlanStatisticDetailById(thisPlanId);
            statisticDtos.add(statisticDto);
        }

        return statisticDtos;
    }

    private List<MyPlanStatisticDetailResponseDto> findAllStatisticDtosByMember(List<MyPlanDetailResponseDto> planList) {
        List<MyPlanStatisticDetailResponseDto> statisticDtos = new ArrayList<>();

        for (MyPlanDetailResponseDto plan : planList) {
            Long thisPlanId = plan.getPlanId();
            MyPlanStatisticDetailResponseDto statisticDto = getPlanStatisticDetailById(thisPlanId);
            statisticDtos.add(statisticDto);
        }

        return statisticDtos;
    }

    // 2023.8.7(월) 4h55
    @Override
    public List<MyPlanDetailResponseDto> findAllActivePlansByMemberMemberId(Long memberId) {
        List<Plan> findPlans = planRepository.findAllActivePlansByMemberMemberId(memberId); // planId 내림차순 정렬
        return planMapper.toMyPlanDetailResponseDtos(findPlans);
    }

    private List<MyPlanDetailResponseDto> findAllPlansByMemberMemberId(Long memberId) {
        List<Plan> findPlans = planRepository.findAllPlansByMemberMemberId(memberId);
        return planMapper.toMyPlanDetailResponseDtos(findPlans);
    }

    private String parseChatGptResponse(String content) {
        return content.replaceAll("\n", "<br>");
    }

    // 2023.8.21(월) 14h 변경
    /*
    private Map<String, Object> getAdditionalBookInfo(String isbn) {
        String itemRequestUrl = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=" + bookApiKey + "&itemIdType=ISBN13&ItemId=" + isbn + "&output=js&Version=20131101&OptResult=Toc";
        //http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttbgreenkey201608001 &itemIdType=ISBN13 &output=js &Version=20131101 &ItemId=9791189909284 &Version=20131101 &OptResult=Toc
        RestTemplate restTemplate = new RestTemplate();
        BooksListSearchResponseDto responseDto = restTemplate.getForObject(itemRequestUrl, BooksListSearchResponseDto.class);

        Map<String, Object> results = new HashMap<>();

        if (!responseDto.getItem().isEmpty()) {
            Integer numOfPages = getNumOfPages(responseDto);
            String tableOfContents = getTableOfContents(responseDto);

            results.put("numOfPages", numOfPages);
            results.put("tableOfContents", tableOfContents);

            return results;
        } else {
            return null;
        }
    }
     */

    private Integer getNumOfPages(String isbn) {
        String itemRequestUrl = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=" + bookApiKey + "&itemIdType=ISBN13&ItemId=" + isbn + "&output=js&Version=20131101";
        RestTemplate restTemplate = new RestTemplate();
        BooksListSearchResponseDto responseDto = restTemplate.getForObject(itemRequestUrl, BooksListSearchResponseDto.class);

        if (!responseDto.getItem().isEmpty()) {
            return responseDto.getItem().get(0).getSubInfo().getItemPage();
        } else {
            return null;
        }
    }
}
