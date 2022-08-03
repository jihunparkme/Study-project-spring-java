package car;

import jdk.nashorn.internal.objects.annotations.Getter;

public abstract class AbstractCar implements Car {
    private double distance;
    private double distancePerLiter;

    public AbstractCar(double distance, double distancePerLiter) {
        this.distance = distance;
        this.distancePerLiter = distancePerLiter;
    }

    @Override
    public double getChargeQuantity() {
        return distance / distancePerLiter;
    }
}