package vendingMachine;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Coin {

    private final int value;

    static final int _500 = 500;
    static final int _100 = 100;
    static final int _50 = 50;
    static final int _10 = 10;

    private static final List<Integer> COIN_SET = Arrays.asList(_500, _100, _50, _10);

    public Coin(final int i) {
        if (!COIN_SET.contains(i)) {
            throw new IllegalArgumentException();
        }
        this.value = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coin coin = (Coin) o;
        return value == coin.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Coin{}";
    }
}
