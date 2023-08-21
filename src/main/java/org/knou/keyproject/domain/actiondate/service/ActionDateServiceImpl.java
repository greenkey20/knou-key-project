package org.knou.keyproject.domain.actiondate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.dto.ActionDatePostRequestDto;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.actiondate.mapper.ActionDateMapper;
import org.knou.keyproject.domain.actiondate.repository.ActionDateRepository;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.knou.keyproject.domain.plan.repository.PlanRepository;
import org.knou.keyproject.domain.plan.service.PlanService;
import org.knou.keyproject.global.utils.PlanStatisticUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.knou.keyproject.global.utils.StringParsingUtils.replaceNewLineWithBr;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ActionDateServiceImpl implements ActionDateService {
    private final PlanRepository planRepository;
    private final ActionDateRepository actionDateRepository;
//    private final ActionDateMapper actionDateMapper;
//    private final PlanService planService;
    private final PlanStatisticUtils planStatisticUtils;

    // 2023.7.29(토) 22h35
    @Override
    @Transactional
    public void deleteActionDatesByPlanId(Long planId) {
        actionDateRepository.deleteAllByPlanPlanId(planId);
    }

    // 2023.7.31(월) 0h10
    @Override
    public ActionDate findByActionDateId(Long actionDateId) {
        return actionDateRepository.findById(actionDateId).orElse(null);
    }

    // 2023.7.31(월) 2h10 -> 3h 저장이 안 되는 것을 보고, 수정..
    @Override
    @Transactional
    public ActionDate saveNewActionDate(ActionDatePostRequestDto requestDto) {
//        ActionDate actionDateToSave = actionDateMapper.toEntity(requestDto);

        ActionDate actionDateToSave = findVerifiedActionDate(requestDto.getActionDateId());

        // 2023.7.31(월) 3h5 나의 생각 = 굳이 바뀌지는 않으니까 update/set 안 해도 될 듯?
//        Plan findPlan = planRepository.findById(requestDto.getPlanId()).orElse(null);
//        actionDateToSave.setPlan(findPlan);

        // numOfYear/Month/Date는 최초 계획 시 만들어진 값을 그대로 유지하기로 함 -> 아래 필요 없음
//        String[] nums = actionDateToSave.getRealActionDate().toString().split("-");

//        actionDateToSave = ActionDate.builder()
//                .numOfYear(nums[0])
//                .numOfMonth(Integer.parseInt(nums[1]))
//                .numOfDate(nums[2])
//                .numOfDay(actionDateToSave.getRealActionDate().getDayOfWeek().getValue())
//                .isDone(true)
//                .build();

        actionDateToSave.setMemo(replaceNewLineWithBr(requestDto.getMemo()));
        actionDateToSave.setRealActionQuantity(requestDto.getRealActionQuantity());
        actionDateToSave.setTimeTakenForRealAction(requestDto.getTimeTakenForRealAction());
        actionDateToSave.setReviewScore(requestDto.getReviewScore());
        actionDateToSave.setRealActionDate(LocalDate.of(requestDto.getRealActionDate().getYear(), requestDto.getRealActionDate().getMonthValue(), requestDto.getRealActionDate().getDayOfMonth()));
        actionDateToSave.setRealStartUnit(requestDto.getRealStartUnit());
        actionDateToSave.setRealEndUnit(requestDto.getRealEndUnit());
        actionDateToSave.setIsDone(true);

        ActionDate savedActionDate = actionDateRepository.save(actionDateToSave);
//        log.info("ActionDateService에서 saveNewActionDate() 되나요? " + savedActionDate);
//        log.info("ActionDateService에서 saveNewActionDate() 되나요? " + savedActionDate.getIsDone());
//        log.info("ActionDateService에서 saveNewActionDate() 되나요? " + savedActionDate.getRealActionDate()); // null이네.. toEntity 하는 것만으로는 저장 안 되었음


        // 2023.8.7(월) 10h15 금번 활동 기록으로 complete 상태가 되는지 체크해야 함
        Long planId = requestDto.getPlanId();
//        Plan findPlan = planService.findVerifiedPlan(planId); // 2023.8.7(월) 10h30 순환참조 발생
        Plan findPlan = planRepository.findById(planId).orElse(null);
        Integer totalQuantity = findPlan.getTotalQuantity();
        Integer accumulatedRealActionQuantity = planStatisticUtils.getAccumulatedRealActionQuantity(planId);
        if (accumulatedRealActionQuantity >= totalQuantity) {
            findPlan.setStatus(PlanStatus.COMPLETE);
            findPlan.setLastStatusChangedAt(LocalDate.now());
        }

        return savedActionDate;
    }

    // 2023.7.31(월) 6h45
    @Override
    @Transactional
    public void deleteActionDate(Long actionDateId) {
        ActionDate actionDateToDelete = actionDateRepository.findById(actionDateId).orElse(null);

        actionDateToDelete.setMemo(null);
        actionDateToDelete.setRealActionQuantity(null);
        actionDateToDelete.setTimeTakenForRealAction(null);
        actionDateToDelete.setReviewScore(null);
        actionDateToDelete.setRealActionDate(null);
        actionDateToDelete.setIsDone(false);

        ActionDate deletedActionDate = actionDateRepository.save(actionDateToDelete);
    }

    @Override
    @Transactional
    public ActionDate updateActionDate(ActionDatePostRequestDto requestDto) {
        ActionDate actionDateToUpdate = findVerifiedActionDate(requestDto.getActionDateId());

        actionDateToUpdate.setRealActionDate(requestDto.getRealActionDate());
        actionDateToUpdate.setRealActionQuantity(requestDto.getRealActionQuantity());
        actionDateToUpdate.setTimeTakenForRealAction(requestDto.getTimeTakenForRealAction());
        actionDateToUpdate.setReviewScore(requestDto.getReviewScore());
        actionDateToUpdate.setMemo(requestDto.getMemo());
        actionDateToUpdate.setIsDone(true);

        return actionDateToUpdate;
    }

    public ActionDate findVerifiedActionDate(Long actionDateId) {
        return actionDateRepository.findById(actionDateId).orElse(null);
    }
}
