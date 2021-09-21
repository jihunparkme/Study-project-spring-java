package baseball;

import baseball.domain.Judgment;
import baseball.domain.NumberGenerator;
import baseball.domain.Referee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * 객체 지향 프로그래밍
 * 1. 기능을 가지고 있는 클래스를 인스턴스화(=객체)
 * 2. 필요한 기능을 (역할에 맞는) 각 인스턴스가 수행 (의인화)
 * 3. 각 결과를 종합
 */
public class Application {
    public static void main(String[] args) {

//        test();

        NumberGenerator generator = new NumberGenerator();
        List<Integer> computer = generator.createRandomNumbers();

        Referee referee = new Referee();

        String result = "";
        while (!result.equals("0 볼 3 스트라이크")) {
            result = referee.compare(computer, askNumbers());
            System.out.println("result = " + result);
        }

        System.out.println("3개의 숫자를 모두 맞히셨습니다! 게임 종료");
    }

    private static void test() {

        NumberGenerator generator = new NumberGenerator();
        List<Integer> randomNumbers = generator.createRandomNumbers();
        System.out.println("randomNumbers = " + randomNumbers);

        Judgment judgment = new Judgment();
        int count = judgment.correctCount(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        System.out.println("count = " + count);

        boolean place = judgment.hasPlace(Arrays.asList(7, 8, 9), 0, 7);
        System.out.println("place = " + place);

        Referee referee = new Referee();
        String compare = referee.compare(Arrays.asList(7, 8, 9), Arrays.asList(1, 2, 3));
        System.out.println("compare = " + compare);
    }

    private static List<Integer> askNumbers() {
        System.out.print("숫자를 입력해 주세요 : ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();

        List<Integer> numbers = new ArrayList<>();
        for (String number : input.split("")) {
            numbers.add(Integer.valueOf(number));
        }

        return numbers;
    }
}
