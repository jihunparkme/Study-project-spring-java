package com.example.springbatchpartitioning

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DateRangePartitionerTest {

    @Test
    @DisplayName("시작일과 종료일 사이의 날짜 수만큼 파티션이 생성되어야 한다")
    fun `the number of partitions must be between two dates`() {
        // given
        val startDate = LocalDate.of(2026, 1, 1)
        val endDate = LocalDate.of(2026, 1, 5)
        val partitioner = DateRangePartitioner(startDate, endDate)

        // when
        val result = partitioner.partition(gridSize = 6)

        // then
        assertThat(result).hasSize(5)
        assertThat(result.keys).containsExactlyInAnyOrder(
            "partition_0", "partition_1", "partition_2", "partition_3", "partition_4"
        )
        assertThat(result["partition_0"]?.getString("targetDate")).isEqualTo("2026-01-01")
        assertThat(result["partition_4"]?.getString("targetDate")).isEqualTo("2026-01-05")
    }

    @Test
    @DisplayName("시작일과 종료일이 같은 경우 1개의 파티션이 생성되어야 한다")
    fun `if both dates are the same, only one partition should be created`() {
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
    fun `If the start date is later than the end date, an empty map must be returned`() {
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