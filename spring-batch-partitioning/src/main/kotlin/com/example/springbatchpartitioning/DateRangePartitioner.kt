package com.example.springbatchpartitioning

import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.item.ExecutionContext
import java.time.LocalDate

class DateRangePartitioner(
    private val startDate: LocalDate,
    private val endDate: LocalDate
) : Partitioner {

    override fun partition(gridSize: Int): Map<String, ExecutionContext> {
        val result = mutableMapOf<String, ExecutionContext>()
        var targetDate = startDate
        var partitionNumber = 0

        while (!targetDate.isAfter(endDate)) {
            val context = ExecutionContext()
            context.putString("targetDate", targetDate.toString())

            result["partition_$partitionNumber"] = context

            targetDate = targetDate.plusDays(1)
            partitionNumber++
        }

        return result
    }
}