package vendingMachine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Red(질문) -> Green(응답) -> Refactor(정제)
 *
 * Red : 시스템에 질문
 * Greend : 사용자가 응답하면서 구현
 * Refactor : 코드 정제
 */
class VendingMachineTest {

    @Test
    void 자판기_생성() {
        VendingMachine vendingMachine = new VendingMachine();
        assertThat(vendingMachine).isNotNull();
    }

    @ParameterizedTest(name = "자판기에 {0}원을 넣으면{0}원이 들어있음을 알 수 있다")
    @ValueSource(ints = {500, 1000})
    void 자판기에_n을_넣으면_n원이_들어있음을_알_수_있다(int changes) {
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.put(changes);
        assertThat(vendingMachine.getChanges()).isEqualTo(changes);
    }

    @Test
    void _1000원이_들어있는_자판기에_500원을_넣으면_1500원이_들어있음을_알_수_있다() {
        VendingMachine vendingMachine = new VendingMachine(1000);
        vendingMachine.put(500);
        assertThat(vendingMachine.getChanges()).isEqualTo(1500);
    }

    @Test
    void _500이_들어있는_자판기_500원을_차감하면_0원이_된다() {
        VendingMachine vendingMachine = new VendingMachine(500);
        vendingMachine.withdraw(500);
        assertThat(vendingMachine.getChanges()).isEqualTo(0);
    }

    @Test
    void _0원이_들어있는_자판기_500원을_차감할_수_없다() {
        VendingMachine vendingMachine = new VendingMachine(0);
        assertThatIllegalStateException().isThrownBy(() -> vendingMachine.withdraw(500));
    }
}