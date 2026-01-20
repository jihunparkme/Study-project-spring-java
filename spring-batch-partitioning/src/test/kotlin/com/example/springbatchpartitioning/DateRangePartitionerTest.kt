package com.example.springbatchpartitioning

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DateRangePartitionerTest {

    @Test
    @DisplayName("시작일과 종료일 사이의 날짜 수만큼 파티션이 생성되어야 한다")
    fun partition_NormalRange_Success() {
        // given: 2026-01-01부터 2026-01-05까지 (총 5일)
        val startDate = LocalDate.of(2026, 1, 1)
        val endDate = LocalDate.of(2026, 1, 5)
        val partitioner = DateRangePartitioner(startDate, endDate)

        // when
        val result = partitioner.partition(gridSize = 6) // gridSize는 로직상 영향을 주지 않음

        // then
        assertThat(result).hasSize(5)
        assertThat(result.keys).containsExactlyInAnyOrder(
            "partition_0", "partition_1", "partition_2", "partition_3", "partition_4"
        )

        // 특정 날짜가 올바르게 들어갔는지 확인
        assertThat(result["partition_0"]?.getString("targetDate")).isEqualTo("2026-01-01")
        assertThat(result["partition_4"]?.getString("targetDate")).isEqualTo("2026-01-05")
    }

    @Test
    @DisplayName("시작일과 종료일이 같은 경우 1개의 파티션이 생성되어야 한다")
    fun partition_SameDate_ReturnsOnePartition() {
        // given
        val date = LocalDate.of(2026, 1, 1)
        val partitioner = DateRangePartitioner(date, date)

        // when
        val result = partitioner.partition(10)

        // then
        assertThat(result).hasSize(1)
        assertThat(result["partition_0"]?.getString("targetDate")).isEqualTo("2026-01-01")
    }

    @Test
    @DisplayName("시작일이 종료일보다 늦은 경우 빈 맵을 반환해야 한다")
    fun partition_InvalidRange_ReturnsEmptyMap() {
        // given
        val startDate = LocalDate.of(2026, 1, 10)
        val endDate = LocalDate.of(2026, 1, 1)
        val partitioner = DateRangePartitioner(startDate, endDate)

        // when
        val result = partitioner.partition(6)

        // then
        assertThat(result).isEmpty()
    }
}