package coordinate;

import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RectangleTest {

    private List<Point> points;
    private Rectangle rectangle;

    @BeforeEach
    void setUp() {
        List<Point> points = Arrays.asList(new Point(10, 10), new Point(22, 10), new Point(22, 18), new Point(10, 18));
        rectangle = new Rectangle(points);
    }

    @Test
    @DisplayName("직사각형 생성")
    void generate_rectangle() throws Exception {
        assertThat(rectangle).isEqualTo(new Rectangle(points));
    }

    @Test
    @DisplayName("직사각형 생성 오류")
    void generate_rectangle_error() throws Exception {
        List<Point> error_points = Arrays.asList(new Point(1, 1), new Point(1, 3), new Point(3, 1), new Point(3, 5));
        assertThatThrownBy(() -> {
            new Rectangle(error_points);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 포인트를 가지고 있는지 확인")
    void check_have_a_particular_point() throws Exception {
        assertThat(rectangle.hasPoint(10, 10)).isTrue();
        assertThat(rectangle.hasPoint(20, 18)).isFalse();
    }

    @Test
    @DisplayName("직사각형 넓이 계산")
    void calculate_rectangle() throws Exception {
        assertThat(rectangle.area()).isEqualTo(28);
    }

    @AfterEach
    void tearDown() {
        points = null;
        rectangle = null;
    }
}
