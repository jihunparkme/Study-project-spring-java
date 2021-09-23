package vendingMachine;

import java.util.ArrayList;
import java.util.List;

public class Changes {
    private int amount;

    public Changes(int value) {
        this.amount = value;
    }

    List<CoinSet> coin() {
        List<CoinSet> list = new ArrayList<>();
        for (CoinSet coin : CoinSet.highestOrder()) {
            final int count = amount / coin.value;
            for (int i = 0; i < count; i++) {
                list.add(coin);
            }
            amount = amount - (count * coin.value);
        }
        return list;
    }
}