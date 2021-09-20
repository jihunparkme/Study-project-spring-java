package baseball; // 패키지명을 통해 어떤 프로그램인지 한 눈에 알아볼 수 있도록 !!!

import baseball.domain.Calculator;

public class Application {
    public static void main(String[] args) {
        Calculator teacher = new Calculator(); // Calculator class 의 인스턴스
        System.out.println(teacher.add(1, 3));
        System.out.println(teacher.result);

        Calculator calculator = new Calculator();
        System.out.println(calculator.add(2000, 21));
        System.out.println(calculator.result);

        System.out.println(teacher.shareResult);
    }
}
