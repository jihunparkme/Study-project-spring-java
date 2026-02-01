package com.example.springbatchpartitioning.compare

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.MongoItemReader
import org.springframework.batch.item.data.MongoItemWriter
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDateTime

@Configuration
class PaymentBasicConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val mongoTemplate: MongoTemplate
) {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val CHUNK_SIZE = 1_000
    }

    @Bean
    fun simplePaymentJob(simpleStep: Step): Job =
        JobBuilder("simplePaymentJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(simpleStep)
            .build()

    @Bean
    fun simpleStep(
        simpleReader: ItemReader<PaymentLedger>,
        simpleProcessor: ItemProcessor<PaymentLedger, PaymentLedger>,
        simpleWriter: ItemWriter<PaymentLedger>
    ): Step =
        StepBuilder("simpleStep", jobRepository)
            .chunk<PaymentLedger, PaymentLedger>(CHUNK_SIZE, transactionManager)
            .reader(simpleReader)
            .processor(simpleProcessor)
            .writer(simpleWriter)
            .build()

    @Bean
    @StepScope
    fun simpleReader(
        @Value("#{jobParameters['fromIndex']}") fromIndex: Long,
        @Value("#{jobParameters['toIndex']}") toIndex: Long
    ): MongoItemReader<PaymentLedger> {
        val query = Query().apply {
            addCriteria(
                Criteria.where("orderNumber").gte(fromIndex).lte(toIndex)
            )
        }
        log.info(">>> [Reader Query] 파티션 범위: $fromIndex ~ $toIndex, 쿼리내용: $query")

        return MongoItemReaderBuilder<PaymentLedger>()
            .name("simpleReader")
            .template(mongoTemplate)
            .collection("payment_ledger")
            .targetType(PaymentLedger::class.java)
            .query(query)
            .sorts(mapOf("orderNumber" to Sort.Direction.ASC))
            .pageSize(CHUNK_SIZE)
            .build()
    }

    @Bean
    fun simpleProcessor(): ItemProcessor<PaymentLedger, PaymentLedger> = ItemProcessor { item ->
        log.info("Processing item: ${item.transactionId}")
        item.copy(updatedAt = LocalDateTime.now())
    }

    @Bean
    fun simpleWriter(): MongoItemWriter<PaymentLedger> =
        MongoItemWriter<PaymentLedger>().apply {
            setTemplate(mongoTemplate)
            setCollection("payment_ledger_backup")
        }
}