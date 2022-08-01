package coordinate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

public class LineTest {

    private Line line;

    @BeforeEach
    void setUp() {
        List<Point> points = Arrays.asList(new Point(10, 10), new Point(14, 15));
        line = new Line(points);
    }

    @Test
    @DisplayName("주어진 포인트를 가지고 있는지 확인")
    void check_have_a_particular_point() throws Exception {
        assertThat(line.hasPoint(14, 15)).isTrue();
        assertThat(line.hasPoint(12, 15)).isFalse();
    }

    @Test
    @DisplayName("선의 길이 계산")
    void calculate_the_length_of_a_line() throws Exception {
        assertThat(line.area()).isEqualTo(6.403124, offset(0.00099));
    }
}
