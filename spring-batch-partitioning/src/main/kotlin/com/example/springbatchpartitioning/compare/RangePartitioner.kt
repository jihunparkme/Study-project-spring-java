package com.example.springbatchpartitioning.compare

import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.item.ExecutionContext
import kotlin.math.min

class RangePartitioner(
    private val totalCount: Int,
    private val stepSize: Int
) : Partitioner {
    override fun partition(gridSize: Int): Map<String, ExecutionContext> {
        // 1부터 totalAmount까지 stepSize만큼 건너뛰며 범위를 생성
        return (1 until totalCount + 1 step stepSize)
            // index: 파티션 번호, start: 시작값
            .mapIndexed { index, start ->
                val end = min(start + stepSize - 1, totalCount)

                "partition$index" to ExecutionContext().apply {
                    putInt("fromIndex", start)
                    putInt("toIndex", end)
                }
            }.toMap()
    }
}