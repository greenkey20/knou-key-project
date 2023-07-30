package org.knou.keyproject.domain.actiondate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.dto.ActionDatePostRequestDto;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.actiondate.mapper.ActionDateMapper;
import org.knou.keyproject.domain.actiondate.repository.ActionDateRepository;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ActionDateServiceImpl implements ActionDateService {
    private final PlanRepository planRepository;
    private final ActionDateRepository actionDateRepository;
    private final ActionDateMapper actionDateMapper;

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

        ActionDate actionDateToSave = actionDateRepository.findById(requestDto.getActionDateId()).orElse(null);

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

        actionDateToSave.setMemo(requestDto.getMemo());
        actionDateToSave.setRealActionQuantity(requestDto.getRealActionQuantity());
        actionDateToSave.setTimeTakenForRealAction(requestDto.getTimeTakenForRealAction());
        actionDateToSave.setReviewScore(requestDto.getReviewScore());
        actionDateToSave.setRealActionDate(requestDto.getRealActionDate());
        actionDateToSave.setIsDone(true);

        ActionDate savedActionDate = actionDateRepository.save(actionDateToSave);
//        log.info("ActionDateService에서 saveNewActionDate() 되나요? " + savedActionDate);
//        log.info("ActionDateService에서 saveNewActionDate() 되나요? " + savedActionDate.getIsDone());
//        log.info("ActionDateService에서 saveNewActionDate() 되나요? " + savedActionDate.getRealActionDate()); // null이네.. toEntity 하는 것만으로는 저장 안 되었음

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
}
