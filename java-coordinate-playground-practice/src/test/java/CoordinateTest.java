import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CoordinateTest {

    @Test
    @DisplayName("좌표는 1 부터 24 사이의 숫자로만 입력")
    void input_between_1_to_24() throws Exception {
        //given
        Coordinate coordinate = new Coordinate(24);
        assertThat(coordinate.getNum()).isEqualTo(24);

        assertThatThrownBy(() -> {
            new Coordinate(0);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The argument should be between 1 and 24");

        assertThatThrownBy(() -> {
            new Coordinate(25);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The argument should be between 1 and 24");
    }
}