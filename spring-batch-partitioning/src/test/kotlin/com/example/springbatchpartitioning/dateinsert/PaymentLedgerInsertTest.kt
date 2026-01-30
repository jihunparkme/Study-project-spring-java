package com.example.springbatchpartitioning.dateinsert

import com.example.springbatchpartitioning.compare.PaymentDetail
import com.example.springbatchpartitioning.compare.PaymentLedger
import com.example.springbatchpartitioning.compare.PaymentStatus
import org.junit.jupiter.api.Disabled
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom
import kotlin.test.Test

@Disabled
@ActiveProfiles("test")
@SpringBootTest
class PaymentLedgerInsertTest {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    private val log = LoggerFactory.getLogger(javaClass)
    private val dummyMemo = "A".repeat(1024)

    @Test
    fun insert_payment_ledger() {
        val totalRecords = 100_000_000L
        val batchSize = 100_000
        val startTime = System.currentTimeMillis()

        for (i in 0 until (totalRecords / batchSize)) {
            val bulkOps = mongoTemplate.bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                PaymentLedger::class.java
            )

            val ledgers = (1..batchSize).map {
                createRandomLedger(i * batchSize + it)
            }

            bulkOps.insert(ledgers)
            bulkOps.execute()

            if (i % 100 == 0L) {
                val elapsed = (System.currentTimeMillis() - startTime) / 1000
                log.info(">>> 진행도: ${i * batchSize} / $totalRecords (경과: ${elapsed}s)")
            }
        }
    }

    private fun createRandomLedger(index: Long): PaymentLedger {
        val random = ThreadLocalRandom.current()
        return PaymentLedger(
            transactionId = UUID.randomUUID().toString(),
            orderId = "ORD-${1000000 + index}",
            userId = "USER-${random.nextInt(1, 1000000)}",
            amount = random.nextLong(1000, 1000000),
            status = PaymentStatus.values()[random.nextInt(0, 4)],
            paymentMethod = listOf("CARD", "NAVER_PAY", "KAKAO_PAY", "TOSS")[random.nextInt(0, 4)],
            memo = dummyMemo,
            details = listOf(
                PaymentDetail(itemName = "상품A", quantity = 1, unitPrice = 5000),
                PaymentDetail("상품B", random.nextInt(1, 5), 12000)
            ),
            auditLog = listOf(
                "Payment requested at ${LocalDateTime.now()}",
                "PG SDK initialized",
                "Transaction authorized by bank"
            )
        )
    }
}