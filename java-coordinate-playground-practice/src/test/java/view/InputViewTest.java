package view;

import coordinate.Figure;
import coordinate.FigureFactory;
import coordinate.Point;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class InputViewTest {
    private List<Point> points;

    @BeforeEach
    void setUp() {
        points = new ArrayList<>();
    }

    @Test
    @DisplayName("사용자 입력에 따른 Line 생성")
    void generate_line_based_on_Input() {
        Figure figure = InputView.inputCoordinates("(1, 1) - (2, 2)");
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        assertThat(figure).isEqualTo(FigureFactory.create(points));
    }

    @Test
    @DisplayName("사용자 입력에 따른 Triangle 생성")
    void generate_triangle_based_on_Input() {
        Figure figure = InputView.inputCoordinates("(1, 1) - (3, 2) - (2, 5)");
        points.add(new Point(1, 1));
        points.add(new Point(3, 2));
        points.add(new Point(2, 5));
        assertThat(figure).isEqualTo(FigureFactory.create(points));
    }

    @Test
    @DisplayName("사용자 입력에 따른 Rectangle 생성")
    void generate_ractangle_based_on_Input() {
        Figure figure = InputView.inputCoordinates("(1, 5) - (3, 5) - (1, 3) - (3, 3)");
        points.add(new Point(1, 5));
        points.add(new Point(3, 5));
        points.add(new Point(1, 3));
        points.add(new Point(3, 3));
        assertThat(figure).isEqualTo(FigureFactory.create(points));
    }

    @AfterEach
    void tearDown() {
        points = null;
    }
}