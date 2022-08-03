public class K5 extends Car {

    private final int distance;

    public K5(int distance) {
        this.distance = distance;
    }

    @Override
    double getDistancePerLiter() {
        return CarType.K5.getFuelEfficiency();
    }

    @Override
    double getTripDistance() {
        return this.distance;
    }

    @Override
    String getName() {
        return CarType.K5.getName();
    }
}
