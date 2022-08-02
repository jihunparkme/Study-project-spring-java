package coordinate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FigureFactoryTest {
    private List<Point> points;

    @BeforeEach
    void setUp() {
        points = new ArrayList<>();
    }

    @Test
    @DisplayName("Null을 입력할 경우")
    void input_null_exception() {
        assertThrows(IllegalArgumentException.class, () -> {
            FigureFactory.create(null);
        });
    }

    @Test
    @DisplayName("포인트를 한 개 입력할 경우")
    void input_one_point_exception() {
        points.add(new Point(1, 2));
        assertThrows(IllegalArgumentException.class, () -> {
            FigureFactory.create(points);
        });
    }

    @Test
    @DisplayName("포인트를 다섯 개 이상 입력할 경우")
    void input_more_than_five_point_exception() {
        points.add(new Point(1, 2));
        points.add(new Point(3, 5));
        points.add(new Point(5, 6));
        points.add(new Point(7, 9));
        points.add(new Point(11, 13));
        assertThrows(IllegalArgumentException.class, () -> {
            FigureFactory.create(points);
        });
    }

    @Test
    @DisplayName("포인트가 두 개일 경우 Line 생성")
    void generate_line_if_input_tow_point() {
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        assertThat(FigureFactory.create(points)).isEqualTo(new Line(points));
    }

    @Test
    @DisplayName("포인트가 세 개일 경우 Triangle 생성")
    void generate_triangle_if_input_three_point() {
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(4, 7));
        assertThat(FigureFactory.create(points)).isEqualTo(new Triangle(points));
    }

    @Test
    @DisplayName("포인트가 네 개일 경우 Rectangle 생성")
    void generate_rectangle_if_input_four_point() {
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(1, 4));
        points.add(new Point(3, 2));
        assertThat(FigureFactory.create(points)).isEqualTo(new Rectangle(points));
    }

    @AfterEach
    void tearDown() {
        points = null;
    }
}