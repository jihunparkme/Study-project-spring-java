package vendingMachine;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChangesTest {

    @ParameterizedTest()
    @CsvSource({"10,_10_COIN", "50,_50_COIN", "100,_100_COIN", "500,_500_COIN"})
    void n원이_남아_있다면_n원_동전_1개를_돌려_준다(int value, CoinSet coin) {
        Changes changes = new Changes(value);
        List<CoinSet> coinSet = changes.coin();
        assertThat(coinSet).containsExactlyInAnyOrder(coin);
    }
}