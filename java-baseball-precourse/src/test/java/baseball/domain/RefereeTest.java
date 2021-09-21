package baseball.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

class RefereeTest {
    private static final List<Integer> ANSWER = Arrays.asList(1, 2, 3);
    private Referee referee;

    @BeforeEach
    void setUp() {
        referee = new Referee();
    }

    /**
     * 묶음 테스트
     */
    @ParameterizedTest
    @CsvSource({"1,2,3,0 볼 3 스트라이크", "7,8,9,아웃", "2,3,1,3 볼 0 스트라이크", "1,3,2,2 볼 1 스트라이크"})
    public void compare(int number1, int number2, int number3, String expected) {
        String result = referee.compare(ANSWER, Arrays.asList(number1, number2, number3));
        Assertions.assertThat(result).isEqualTo(expected);
    }

    /**
     * 개별 테스트
     */
    @Test
    void 스트라이크3() {
        String result = referee.compare(ANSWER, Arrays.asList(1, 2, 3));
        Assertions.assertThat(result).isEqualTo("0 볼 3 스트라이크");
    }

    @Test
    void 아웃() {
        String result = referee.compare(ANSWER, Arrays.asList(7, 8, 9));
        Assertions.assertThat(result).isEqualTo("아웃");
    }

    @Test
    void 볼3() {
        String result = referee.compare(ANSWER, Arrays.asList(2, 3, 1));
        Assertions.assertThat(result).isEqualTo("3 볼 0 스트라이크");
    }

    @Test
    void 볼2_스트라이크1() {
        String result = referee.compare(ANSWER, Arrays.asList(1, 3, 2));
        Assertions.assertThat(result).isEqualTo("2 볼 1 스트라이크");
    }
}