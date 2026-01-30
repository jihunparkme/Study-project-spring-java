package com.example.springbatchpartitioning.compare

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.data.MongoItemReader
import org.springframework.batch.item.data.MongoItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
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
    fun simpleStep(): Step =
        StepBuilder("simpleStep", jobRepository)
            .chunk<PaymentLedger, PaymentLedger>(CHUNK_SIZE, transactionManager)
            .reader(simpleReader())
            .processor(simpleProcessor())
            .writer(simpleWriter())
            .build()

    @Bean
    fun simpleReader(): MongoItemReader<PaymentLedger> =
        MongoItemReader<PaymentLedger>().apply {
            setName("simpleReader")
            setTemplate(mongoTemplate)
            setCollection("payment_ledger")
            setTargetType(PaymentLedger::class.java)
            setQuery("{}")
            setSort(mapOf("_id" to Sort.Direction.ASC))
            setPageSize(CHUNK_SIZE)
        }

    @Bean
    fun simpleProcessor(): ItemProcessor<PaymentLedger, PaymentLedger> = ItemProcessor { item ->
        Thread.sleep(1000)
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