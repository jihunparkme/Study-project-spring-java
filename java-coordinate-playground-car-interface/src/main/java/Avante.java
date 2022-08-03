public class Avante extends Car {

    private final int distance;

    public Avante(int distance) {
        this.distance = distance;
    }

    @Override
    double getDistancePerLiter() {
        return CarType.Avante.getFuelEfficiency();
    }

    @Override
    double getTripDistance() {
        return this.distance;
    }

    @Override
    String getName() {
        return CarType.Avante.getName();
    }
}
