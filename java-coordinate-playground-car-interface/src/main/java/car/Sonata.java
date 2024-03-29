package car;

public class Sonata extends AbstractCar {
    private static final double DISTANCE_PER_LITER = 10;
    private String name;

    public Sonata(double distance) {
        super(distance, DISTANCE_PER_LITER);
        this.name = "Sonata";
    }

    @Override
    public String getName() {
        return name;
    }
}