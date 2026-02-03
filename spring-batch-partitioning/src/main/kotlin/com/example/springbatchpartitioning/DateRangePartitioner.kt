package com.example.springbatchpartitioning

import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.item.ExecutionContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class DateRangePartitioner(
    private val startDate: LocalDate,
    private val endDate: LocalDate
) : Partitioner {

    override fun partition(gridSize: Int): Map<String, ExecutionContext> {
        val daysBetween = ChronoUnit.DAYS.between(
            startDate,
            endDate.plusDays(1)
        )

        return (0 until daysBetween).associate { i ->
            val targetDate = startDate.plusDays(i)
            val context = ExecutionContext().apply {
                putString("targetDate", targetDate.toString())
            }
            "partition_$i" to context
        }
    }
}