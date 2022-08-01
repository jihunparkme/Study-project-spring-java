package coordinate;

import lombok.Getter;

import java.util.List;

@Getter
public class Line {
    private final List<Point> points;

    public Line(List<Point> points) {
        this.points = points;
    }

    public boolean hasPoint(int x, int y) {
        return getPoints().stream()
                .anyMatch(point -> point.isSame(x, y));
    }

    public double area() {
        return getPoints().get(0).calculateDistance(getPoints().get(1));
    }
}