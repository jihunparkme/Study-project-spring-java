package coordinate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PointTest {

    @Test
    @DisplayName("포인트 생성")
    void generate_point() throws Exception {
        assertThat(new Point(1, 2)).isEqualTo(new Point(1, 2));
    }

    @Test
    @DisplayName("좌표가 범위를 넘어갈 경우")
    void coordinate_exceed_the_limits() throws Exception {
        assertThatThrownBy(() -> {
            new Point(1, 0);
        }).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            new Point(0, 1);
        }).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            new Point(1, 25);
        }).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            new Point(25, 1);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}