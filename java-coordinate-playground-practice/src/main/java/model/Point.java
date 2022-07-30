package model;

import lombok.Getter;

@Getter
public class Point {
    private final Coordinate x;
    private final Coordinate y;

    public Point(int x, int y) {
        this.x = new Coordinate(x);
        this.y = new Coordinate(y);
    }

    public int getX() {
        return x.getNum();
    }

    public double getDoubleX() {
        return x.getNum() * 1.0;
    }

    public int getY() {
        return y.getNum();
    }

    public double getDoubleY() {
        return y.getNum() * 1.0;
    }
}
