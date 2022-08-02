package coordinate;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode
public abstract class AbstractFigure implements Figure {
    static final String ERROR_FIGURE_NULL = "올바른 Point 값이 아닙니다.";
    private final List<Point> points;

    AbstractFigure(List<Point> points) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException(ERROR_FIGURE_NULL);
        }
        this.points = points;
    }

    @Override
    public boolean hasPoint(int x, int y) {
        return getPoints().stream()
                .anyMatch(point -> point.isSame(x, y));
    }
}
