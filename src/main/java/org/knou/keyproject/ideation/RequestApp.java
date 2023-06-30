package org.knou.keyproject.ideation;

import java.util.Scanner;

// 2023.6.30(금) 1h50

/**
 * 매일 일정한 공부 계획 수립을 도와주는 서비스,
 * 또는 각자의 공부 계획을 공유하고, 평균적인 공부 계획을 세워 알려주는 서비스..
 */
public class RequestApp {
    public void view() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("실행 시작 예정일 > ");
            String inputDate = scanner.nextLine();

            // 2023.7.1(토) 3h25
            /* 로직의 문제, 가정해야 하는 것
            1. 선형적 상관 관계 e.g. 체중 조절이 시간과 정비례하지 않을 수 있음 -> 그런 경우 어떻게 계산해서 + 어떤 정보를 사용자에게 응답할 것인가?
            2. 1회독 후 복습이 필요한 경우
            3. 구간별 난이도가 다른 경우
             */

            // 무엇을 준비하고 계신가요? (측정 가능한 일 or 측정이 어려운 일 or 잘 모르겠어요) + 목표 기간이 이미 정해져 있나요? -> 경우의 수별 예시, 시나리오

            // [목표 대상에 따라]
            // 책, 교과서, 수험서, 악보, 원고, 과목 수, 예상 면접 질문 수, 체중 조절, 단어 암기 등
            System.out.print("목표 페이지 수 > ");
            int inputPages = Integer.parseInt(scanner.nextLine());

            // 기타 정량적 측정이 어려운 과제 e.g. 곰인형 만들기, 그림/디자인 작업, 안무/춤 등

            // [목표 기간 결정 여부]
            // 목표 기간을 아는/목표 기간이 정해진 경우 e.g. 시험, 발표회, 면접 등
            System.out.print("목표 기간 > ");
            int inputDays = Integer.parseInt(scanner.nextLine());

            // 목표 기간을 내가 정할 수 있는 경우 e.g. 자기계발을 위한 독서 등 -> 내가 목표로 하는 기간, 나의 현재 능력/상태/속도에 따라 계산된 기간 등 가능

            // 복습이 필요하다고 생각하시나요? (yes or no or 잘 모르겠어요, 도와주세요)

            // 본인의 가용 시간을 알고 계시나요? (주말만 가능, 주7일 가능, 주3일 가능, 그 때 그 때 다름)

            double ceil = Math.ceil(inputPages / inputDays);
            System.out.println("목표 달성을 위해서는 매일 " + ceil + "페이지를 공부해야 합니다");
        }
    }
}
