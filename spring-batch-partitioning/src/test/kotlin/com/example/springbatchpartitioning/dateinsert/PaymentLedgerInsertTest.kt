package com.example.springbatchpartitioning.dateinsert

import org.junit.jupiter.api.Disabled
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.mapping.Document
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
        val totalRecords = 1_000_000L
        val batchSize = 10_000
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
                PaymentDetail("상품A", 1, 5000),
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

@Document(collection = "payment_ledger")
data class PaymentLedger(
    @Id val id: String? = null,
    val transactionId: String,       // 거래 고유 ID
    val orderId: String,             // 주문 ID
    val userId: String,              // 사용자 ID
    val amount: Long,                // 결제 금액
    val currency: String = "KRW",    // 통화
    val status: PaymentStatus,       // 결제 상태 (READY, PAID, CANCELLED, FAILED)
    val paymentMethod: String,       // 결제 수단 (CARD, TRANSFER, PAY)
    val memo: String,                // 넉넉한 용량을 위한 메모 (더미 텍스트)
    val details: List<PaymentDetail>,// 결제 상세 내역
    val auditLog: List<String>,      // 감사 로그
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class PaymentDetail(
    val itemName: String,
    val quantity: Int,
    val unitPrice: Long
)

enum class PaymentStatus { READY, PAID, CANCELLED, FAILED }