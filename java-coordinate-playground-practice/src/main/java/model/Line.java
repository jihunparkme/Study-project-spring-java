package model;

public class Line {
    private Point pointA;
    private Point pointB;

    public Line(Point pointA, Point pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
    }

    public double calculateDistance() {
        return Math.sqrt(
                preCalculate(pointA.getDoubleX(), pointB.getDoubleX()) +
                preCalculate(pointA.getDoubleY(), pointB.getDoubleY()));
    }

    private double preCalculate(double pointA, double pointB) {
        return Math.pow(Math.abs(pointA - pointB), 2);
    }
}
