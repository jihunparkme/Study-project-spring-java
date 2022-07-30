import lombok.Getter;

@Getter
public class Coordinate {

    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 24;
    private int num;

    public Coordinate(int num) {
        if (verifyScope(num)) {
            throw new IllegalArgumentException("The argument should be between 1 and 24");
        }
        this.num = num;
    }

    private boolean verifyScope(int num) {
        return num < MIN_VALUE || num > MAX_VALUE;
    }
}

