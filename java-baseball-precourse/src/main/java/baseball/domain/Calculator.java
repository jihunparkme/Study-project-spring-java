package baseball.domain;

public class Calculator {

    public static int shareResult = 0; // class 공유 변수 (클래스 변수)

    public int result = 0;  // class 범위 안에서의 변수 관리 (인스턴스 변수)

    public Calculator() {
    }

    public int add(int num1, int num2) {
        result = num1 + num2;
        return result;
    }
}
