package baseball.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumberGenerator {
    public List<Integer> createRandomNumbers() {
        // 3개의 숫자가 담기 때까지
        // 만약 이미 존재하는 숫자라면 담지 않음
        // 만약 존재하지 않는 숫자라면 담음
        List<Integer> numbers = new ArrayList<>();
        while (numbers.size() < 3) {
            int number = new Random().nextInt(9) + 1;
            if (numbers.contains(number)) {
                continue;
            } else {
                numbers.add(number);
            }
        }

        return numbers;
    }
}
