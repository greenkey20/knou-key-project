package org.knou.keyproject.ideation;

import java.util.Scanner;

// 2023.6.30(금) 1h50
public class RequestApp {
    public void view() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("목표 기간 > ");
            int inputDays = Integer.parseInt(scanner.nextLine());

            System.out.print("목표 페이지 수 > ");
            int inputPages = Integer.parseInt(scanner.nextLine());

            double ceil = Math.ceil(inputPages / inputDays);
            System.out.println("목표 달성을 위해서는 매일 " + ceil + "페이지를 공부해야 합니다");
        }
    }
}
