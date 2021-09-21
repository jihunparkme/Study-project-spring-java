package baseball;

import baseball.domain.Judgment;
import baseball.domain.NumberGenerator;
import baseball.domain.Referee;

import java.util.Arrays;
import java.util.List;

/**
 * 객체 지향 프로그래밍
 * 1. 기능을 가지고 있는 클래스를 인스턴스화(=객체)
 * 2. 필요한 기능을 (역할에 맞는) 각 인스턴스가 수행 (의인화)
 * 3. 각 결과를 종합
 */
public class Application {
    public static void main(String[] args) {
        NumberGenerator generator = new NumberGenerator();
        List<Integer> randomNumbers = generator.createRandomNumbers();
        System.out.println("randomNumbers = " + randomNumbers);

        Judgment judgment = new Judgment();
        int count = judgment.correctCount(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        System.out.println("count = " + count);

        boolean place = judgment.hasPlace(Arrays.asList(7, 8, 9), 0, 7);
        System.out.println("place = " + place);

        Referee referee = new Referee();
        String compare = referee.compare(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        System.out.println("compare = " + compare);
    }
}
