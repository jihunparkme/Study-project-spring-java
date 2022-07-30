package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class LineTest {

    @Test
    @DisplayName("두 점 사이의 거리")
    void calculate_distance() throws Exception {
        //given
        Line line = new Line(new Point(10, 10), new Point(14, 15));

        //when
        double result = line.calculateDistance();

        //then
        assertThat(result).isEqualTo(6.403124, offset(0.00099));
    }
}
