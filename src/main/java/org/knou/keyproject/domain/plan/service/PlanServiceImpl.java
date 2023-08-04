package org.knou.keyproject.domain.plan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.actiondate.repository.ActionDateRepository;
import org.knou.keyproject.domain.actiondate.service.ActionDateService;
import org.knou.keyproject.domain.chatgpt.dto.ChatGptRequestDto;
import org.knou.keyproject.domain.chatgpt.dto.ChatGptResponseDto;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.repository.MemberRepository;
import org.knou.keyproject.domain.member.service.MemberService;
import org.knou.keyproject.domain.plan.dto.*;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.knou.keyproject.domain.plan.mapper.PlanMapper;
import org.knou.keyproject.domain.plan.repository.PlanRepository;
import org.knou.keyproject.global.utils.Calendar;
import org.knou.keyproject.global.utils.calculator.Calculator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// 2023.7.23(일) 22h
@Slf4j
@RequiredArgsConstructor
@PropertySource(value = "classpath:application-local.yml")
@Transactional(readOnly = true)
@Service
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final PlanMapper planMapper;
    private final ActionDateRepository actionDateRepository;
    private final MemberService memberService;
    private final ActionDateService actionDateService;
    private final Calendar calendar;

    // 2023.8.2(수) 1h50 ChatGpt 호출 관련 추가
    @Qualifier("openaiRestTemplate")
    private final RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Override
    @Transactional
    public Plan saveNewPlan(PlanPostRequestDto requestDto) {
        Plan planToCalculate = planMapper.toEntity(requestDto);

        if (requestDto.getMemberId() != null) {
            planToCalculate.setMember(memberRepository.findById(requestDto.getMemberId()).orElse(null));
        }

        Plan calculatedPlan = new Calculator().calculateNewPlan(planToCalculate);
        log.info("계산 결과 시작일 = " + calculatedPlan.getStartDate()); // 2023.7.29(토) 4h25 '계산 결과 시작일 = 2023-07-29' 찍힘
        log.info("계산 결과 활동일 중 첫번째 것 = " + calculatedPlan.getActionDatesList().get(0).getDateType().toString()); // 2023.7.29(토) 4h25 actionDatesList()가 비었다고 한다..
//        System.out.println("이거 안 찍히나?" + calculatedPlan.getActionDatesList().get(0).getDateType().toString()); // 2023.7.28(금) 3h15 이거 안 찍히나?ACTION 찍히는데

        // 2023.7.29(토) 4h20
        actionDateRepository.saveAll(calculatedPlan.getActionDatesList());

        return planRepository.save(calculatedPlan);
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
                // 2023.7.29(토) 21h30 '오늘'을 임의의 시작일로 계산했던 actionDatesList 삭제
//                actionDateRepository.deleteAll(findPlan.getActionDatesList()); // 2023.7.29(토) 22h 이렇게는 delete 쿼리가 안 나갔음
                actionDateService.deleteActionDatesByPlanId(findPlan.getPlanId());
                findPlan.setActionDatesList(null);
            }

            findPlan.setStartDate(requestDto.getStartDate());
        }

        log.info("planService에서 calculator 호출 전 findPlan.deadline date" + findPlan.getDeadlineDate());
        findPlan = new Calculator().calculateRealNewPlan(findPlan); // 2023.7.29(토) 1h25 나의 생각 = 여기서 actionDate들 다시 set됨 = 이게 insert가 됨 -> 나의 질문 = 이걸 update되게 하려면 어떻게 해야 하지? 현재 repository 거치지 않아서 그런가?
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

        // 2023.7.29(토) 0h15 jpa update를 어떻게 하는 건지 갑자기 정확히 알지/이해 못하는 것 같아서 googling -> https://study-easy-coding.tistory.com/143
        // setter로 값 변경하면 변경 감지해서 수정 쿼리 날려 db에 반영 = dirty checking
//        planRepository.save(findPlan);
        // 2023.7.29(토) 0h35 나의 궁금증 = 위 save() 호출 안 하는데, 왜 아직도 새로 저장되지..?
    }

    // 2023.7.29(토) 22h20 추가 = 계산 결과 저장을 위해 로그인 하고 왔을 때 actionDates가 추가로 저장(추가로 insert문들이 나가고 있었음)되지 않도록 하기 위해 = 이 경우에는 해당 memberId도 member만 변경해주면 됨
    @Override
    @Transactional
    public void saveMyNewPlanAfterLogin(MyPlanPostRequestDto requestDto) {
        Plan findPlan = findVerifiedPlan(requestDto.getPlanId());
        findPlan.setMember(memberService.findVerifiedMember(requestDto.getMemberId()));
    }

    public Plan findVerifiedPlan(Long planId) {
        return planRepository.findById(planId).orElse(null);
    }

    // 2023.7.24(월) 17h20 자동 기본 구현만 해둠 -> 23h10 내용 구현
    @Override
    public List<MyPlanListResponseDto> findPlansByMember(Long memberId, int currentPage, int size) {
        Member findMember = memberRepository.findById(memberId).orElse(null);
        return planRepository.findAllByMemberMemberId(findMember.getMemberId(), PageRequest.of(currentPage - 1, size, Sort.by("planId").descending()))
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

    // 2023.8.4(금) 22h50 plan 상세보기 요청 controller에 대응하기 위한 메서드 + JSP에서 비즈니스 로직 최대한 빼기 위해 2023.8.5(토) 1H30 추가
    @Override
    public MyPlanStatisticDetailResponseDto getPlanStatisticDetailById(Long planId) {
        Plan findPlan = findVerifiedPlan(planId);
        LocalDate startDate = findPlan.getStartDate();
        LocalDate lastStatusChangedAt = findPlan.getLastStatusChangedAt();

        int accumulatedRealActionQuantity = getAccumulatedRealActionQuantity(planId);
        int accumulatedPlanActionQuantity = getAccumulatedPlanActionQuantity(planId, startDate);
        int quantityDifferenceBetweenPlanAndReal = getQuantityDifferenceBetweenPlanAndReal(accumulatedPlanActionQuantity, accumulatedRealActionQuantity);

        int totalQuantity = findPlan.getTotalQuantity();
        int quantityToEndPlan = getQuantityToEndPlan(totalQuantity, accumulatedRealActionQuantity);

        double ratioOfQuantityToEndPlan = getRatioOfQuantityToEndPlan(accumulatedRealActionQuantity, totalQuantity);

        int accumulatedNumOfActions = getAccumulatedNumOfActions(planId);
        int numOfActionsToEndPlan = getNumOfActionsToEndPlan(findPlan.getTotalNumOfActions(), accumulatedNumOfActions);

        int accumulatedPlanActionQuantityBeforePause = getAccumulatedPlanActionQuantityBeforePause(planId, startDate, lastStatusChangedAt);

        return MyPlanStatisticDetailResponseDto.builder()
                .accumulatedRealActionQuantity(accumulatedRealActionQuantity)
                .accumulatedPlanActionQuantity(accumulatedPlanActionQuantity)
                .quantityDifferenceBetweenPlanAndReal(quantityDifferenceBetweenPlanAndReal)
                .quantityToEndPlan(quantityToEndPlan)
                .ratioOfQuantityToEndPlan(formatPercentage(ratioOfQuantityToEndPlan))
                .accumulatedNumOfActions(getAccumulatedNumOfActions(planId))
                .numOfActionsToEndPlan(numOfActionsToEndPlan)
                .averageTimeTakenForRealAction(getAverageTimeTakenForRealAction(planId))
                .accumulatedPlanActionQuantityBeforePause(accumulatedPlanActionQuantityBeforePause)
                .build();
    }

    // 2023.8.5(토) 4h10 숫자 null -> 0으로 처리하기 위해 만듦
    private int makeNullAsZero(Integer num) {
        if (num != null) {
            return num;
        }

        return 0;
    }

    private Integer getAccumulatedRealActionQuantity(Long planId) {
        return makeNullAsZero(actionDateRepository.getAccumulatedRealActionQuantity(planId));
    }

    private Integer getAccumulatedPlanActionQuantity(Long planId, LocalDate startDate) {
        return makeNullAsZero(actionDateRepository.getAccumulatedPlanActionQuantity(planId, startDate));
    }

    private Integer getAccumulatedPlanActionQuantityBeforePause(Long planId, LocalDate startDate, LocalDate lastStatusChangedAt) {
        return makeNullAsZero(actionDateRepository.getAccumulatedPlanActionQuantityBeforePause(planId, startDate, lastStatusChangedAt));
    }

    private Integer getQuantityDifferenceBetweenPlanAndReal(Integer plan, Integer real) {
        return plan - real; // 양의 정수 = 계획보다 실행이 뒤처지고 있음 vs 음의 정수 = 계획보다 더 많이 실행해서 일정보다 앞서고 있음
    }

    private Integer getQuantityToEndPlan(Integer total, Integer real) {
        return total - real;
    }

    private Double getRatioOfQuantityToEndPlan(Integer real, Integer total) {
        return (1 - (double) real / total) * 100;
    }

    private String formatPercentage(Double num) {
        DecimalFormat df = new DecimalFormat("###.#");
        return df.format(num);
    }

    private Integer getNumOfActionsToEndPlan(Integer total, Integer real) {
        return total - real;
    }

    private Integer getAverageTimeTakenForRealAction(Long planId) {
        return actionDateRepository.getAverageTimeTakenForRealAction(planId);
    }

    private Integer getAccumulatedNumOfActions(Long planId) {
        return actionDateRepository.getAccumulatedNumOfActions(planId);
    }

    // 2023.7.31(월) 19h20
//    @Value("${aladin.requesturl}")
//    public static String listRequestUrl;

    @Override
    public List<BookInfoDto> searchBookTitle(String bookSearchKeyword) {
        // 알라딘 도서 검색 open API 호출 -> json data 결과 얻기 -> json data 결과 얻어 item에 해당하는 값들을 가져옴
        String listRequestUrl = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbgreenkey201608001&Query=" + bookSearchKeyword + "&QueryType=Keyword&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101";

        RestTemplate restTemplate = new RestTemplate();
        BooksListSearchResponseDto responseDto = restTemplate.getForObject(listRequestUrl, BooksListSearchResponseDto.class, bookSearchKeyword);

        List<BooksListSearchResponseDto.Item> items = responseDto.getItem();

        List<BookInfoDto> bookInfoDtos = new ArrayList<>();

        for (BooksListSearchResponseDto.Item item : items) {
            String isbn = item.getIsbn13();
            if (isbn.length() == 13) {
                Integer numOfPages = getNumOfPages(isbn);

                BookInfoDto bookInfoDto = BookInfoDto.builder()
                        .title(item.getTitle())
                        .author(item.getAuthor())
                        .pubDate(item.getPubDate())
                        .description(item.getDescription())
                        .isbn13(item.getIsbn13())
                        .cover(item.getCover())
                        .publisher(item.getPublisher())
                        .numOfPages(numOfPages)
                        .build();

                log.info("이번에 담기는 item = " + bookInfoDto);
                bookInfoDtos.add(bookInfoDto);
            }
        }

        return bookInfoDtos;
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
        return parseChatGptResponse(content);
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

    private String parseChatGptResponse(String content) {
        return content.replaceAll("\n", "<br>");
    }

    private Integer getNumOfPages(String isbn) {
        String itemRequestUrl = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttbgreenkey201608001&itemIdType=ISBN&ItemId=" + isbn + "&output=js&Version=20131101";
        RestTemplate restTemplate = new RestTemplate();
        BooksListSearchResponseDto responseDto = restTemplate.getForObject(itemRequestUrl, BooksListSearchResponseDto.class);

        if (!responseDto.getItem().isEmpty()) {
            return responseDto.getItem().get(0).getSubInfo().getItemPage();
        } else {
            return null;
        }
    }
}
