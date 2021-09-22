package vendingMachine;

public enum CoinSet {
    _500_COIN(500), _100_COIN(100), _50_COIN(50), _10_COIN(10);

    int value;

    CoinSet(int value) {
        this.value = value;
    }
}
