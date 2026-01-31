package com.example.springbatchpartitioning.compare

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RangePartitionerTest {

    @Test
    @DisplayName("10000을 1000씩 나누면 10개의 파티션이 생성되어야 한다")
    fun partitionTest() {
        // given
        val totalAmount = 10000
        val stepSize = 1000
        val partitioner = RangePartitioner(totalAmount, stepSize)

        // when
        // Partitioner 인터페이스의 gridSize 파라미터는 로직에서 사용하지 않으므로 0을 전달
        val result = partitioner.partition(0)

        // then
        // 1. 파티션 개수 검증 (10000 / 1000 = 10)
        assertThat(result).hasSize(10)

        // 2. 첫 번째 파티션 범위 검증 (0 ~ 999)
        val firstContext = result["partition0"]
        assertThat(firstContext?.getInt("fromIndex")).isEqualTo(0)
        assertThat(firstContext?.getInt("toIndex")).isEqualTo(999)

        // 3. 마지막 파티션 범위 검증 (9000 ~ 9999)
        val lastContext = result["partition9"]
        assertThat(lastContext?.getInt("fromIndex")).isEqualTo(9000)
        assertThat(lastContext?.getInt("toIndex")).isEqualTo(9999)
    }

    @Test
    @DisplayName("나머지가 발생하는 경우 마지막 파티션의 끝값은 totalAmount - 1 이어야 한다")
    fun partitionRemainderTest() {
        // given
        val totalAmount = 10500
        val stepSize = 1000
        val partitioner = RangePartitioner(totalAmount, stepSize)

        // when
        val result = partitioner.partition(0)

        // then
        // 1. 파티션 개수 검증 (10000까지 10개 + 남은 500에 대해 1개 = 총 11개)
        assertThat(result).hasSize(11)

        // 2. 마지막 파티션(11번째) 범위 검증 (10000 ~ 10499)
        val lastContext = result["partition10"]
        assertThat(lastContext?.getInt("fromIndex")).isEqualTo(10000)
        assertThat(lastContext?.getInt("toIndex")).isEqualTo(10499) // totalAmount - 1
    }
}