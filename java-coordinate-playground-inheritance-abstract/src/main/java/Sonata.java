public class Sonata extends Car {

    private final int distance;

    public Sonata(int distance) {
        this.distance = distance;
    }

    @Override
    double getDistancePerLiter() {
        return CarType.Sonata.getFuelEfficiency();
    }

    @Override
    double getTripDistance() {
        return this.distance;
    }

    @Override
    String getName() {
        return CarType.Sonata.getName();
    }
}
