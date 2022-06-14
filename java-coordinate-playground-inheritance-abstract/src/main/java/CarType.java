public enum CarType {
    Sonata("Sonata", 10),
    Avante("Avante", 15),
    K5("K5", 13);

    private final String name;
    private final int fuelEfficiency;

    CarType(String name, int fuelEfficiency) {
        this.name = name;
        this.fuelEfficiency = fuelEfficiency;
    }

    public int getFuelEfficiency() {
        return fuelEfficiency;
    }

    public String getName() {
        return name;
    }
}
