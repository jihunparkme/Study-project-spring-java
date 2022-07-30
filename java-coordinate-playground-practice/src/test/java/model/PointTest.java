package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PointTest {

    @Test
    @DisplayName("좌표는 1 부터 24 사이의 숫자로만 입력")
    void input_between_1_to_24() throws Exception {
        //given
        Point position = new Point(1, 24);
        assertThat(position.getX()).isEqualTo(new Coordinate(1));
        assertThat(position.getY()).isEqualTo(new Coordinate(24));

        //when
        assertThatThrownBy(() -> {
            new Point(1, 25);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The argument should be between 1 and 24");

        assertThatThrownBy(() -> {
            new Point(0, 24);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The argument should be between 1 and 24");
    }
}
