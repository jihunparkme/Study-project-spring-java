import car.Car;

import java.util.ArrayList;
import java.util.List;

public class RentCompany {

    private static final String NEWLINE = System.getProperty("line.separator");
    private static RentCompany rentCompany = null;

    private List<Car> cars = new ArrayList<>();

    public static RentCompany create() {
        if (rentCompany == null) {
            rentCompany = new RentCompany();
        }

        return rentCompany;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public String generateReport() {
        StringBuffer sb = new StringBuffer();

        for (Car car : cars) {
            sb.append(car.getName()).append(" : ")
                    .append((int) car.getChargeQuantity()).append("리터")
                    .append(NEWLINE);
        }

        return sb.toString();
    }
}
