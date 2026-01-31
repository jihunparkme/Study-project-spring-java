package com.example.springbatchpartitioning.compare

import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.item.ExecutionContext
import kotlin.math.min

class RangePartitioner(
    private val totalAmount: Int,
    private val stepSize: Int
) : Partitioner {

    override fun partition(gridSize: Int): Map<String, ExecutionContext> {
        // 0부터 totalAmount-1까지 stepSize만큼 건너뛰며 범위를 생성
        return (0 until totalAmount step stepSize)
            // 파티션 번호(index)와 시작값(start
            .mapIndexed { index, start ->
                val end = min(start + stepSize - 1, totalAmount - 1)

                "partition$index" to ExecutionContext().apply {
                    putInt("fromIndex", start)
                    putInt("toIndex", end)
                }
            }.toMap()
    }
}