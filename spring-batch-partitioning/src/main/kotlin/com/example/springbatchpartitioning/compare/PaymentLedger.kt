package com.example.springbatchpartitioning.compare

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "payment_ledger")
data class PaymentLedger(
    @Id val id: String? = null,
    val transactionId: String,       // 거래 고유 ID
    val orderId: String,             // 주문 ID
    val orderNumber: Long,             // 주문 ID
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