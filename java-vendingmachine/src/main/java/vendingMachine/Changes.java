package vendingMachine;

import java.util.Arrays;
import java.util.List;

public class Changes {
    private int value;

    public Changes(int value) {
        this.value = value;
    }

    List<CoinSet> coin() {
        if (this.value == 10) {
            return Arrays.asList(CoinSet._10_COIN);
        }
        if (this.value == 50) {
            return Arrays.asList(CoinSet._50_COIN);
        }
        if (this.value == 100) {
            return Arrays.asList(CoinSet._100_COIN);
        }
        if (this.value == 500) {
            return Arrays.asList(CoinSet._500_COIN);
        }
        return Arrays.asList();
    }
}
