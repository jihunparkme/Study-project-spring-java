package coordinate;

import lombok.Getter;

import java.util.List;

@Getter
public class Line extends AbstractFigure {

    Line(List<Point> points) {
        super(points);
    }

    @Override
    public double area() {
        return getPoints().get(0).calculateDistance(getPoints().get(1));
    }

    @Override
    public String getAreaInfo() {
        throw new UnsupportedOperationException("Line#getAreaInfo not implemented yet !!");
    }
}