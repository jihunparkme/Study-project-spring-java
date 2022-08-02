package coordinate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {
    private List<Point> points;
    private Triangle triangle;

    @BeforeEach
    void setUp() {
        points = Arrays.asList(new Point(1, 1), new Point(2, 10), new Point(7, 5));
        triangle = new Triangle(points);
    }

    @Test
    @DisplayName("삼각형 생성")
    void generate_triangle() {
        assertThat(triangle).isEqualTo(new Triangle(points));
    }

    @Test
    @DisplayName("삼각형 생성 오류")
    void generate_triangle_exception() {
        List<Point> illegalPoints = Arrays.asList(new Point(1, 1), new Point(2, 2), new Point(3, 3));
        assertThrows(IllegalArgumentException.class, () -> {
            new Rectangle(illegalPoints);
        });
    }

    @Test
    @DisplayName("특정 포인트를 가지고 잇는지 확인")
    void check_have_a_particular_point() {
        assertThat(triangle.hasPoint(1, 1)).isTrue();
        assertThat(triangle.hasPoint(1, 3)).isFalse();
    }

    @Test
    @DisplayName("삼각형 넓이 계산")
    void calculate_triangle_area() {
        assertThat(triangle.area()).isEqualTo(25.000, offset(0.00099));
    }

    @AfterEach
    void tearDown() {
        points = null;
        triangle = null;
    }
}