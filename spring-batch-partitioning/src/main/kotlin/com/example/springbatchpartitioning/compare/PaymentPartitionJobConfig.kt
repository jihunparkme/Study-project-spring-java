package com.example.springbatchpartitioning.compare

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.partition.PartitionHandler
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.MongoCursorItemReader
import org.springframework.batch.item.data.builder.MongoCursorItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDateTime

@Configuration
class PaymentPartitionJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val mongoTemplate: MongoTemplate
) {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val CHUNK_SIZE = 1_000
    }

    // 1. Job 설정
    @Bean
    fun paymentPartitionJob(
        paymentManagerStep: Step
    ): Job {
        return JobBuilder("paymentPartitionJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(paymentManagerStep)
            .build()
    }

    // 2. Manager Step (Master) 설정
    @Bean
    fun paymentManagerStep(
        partitionHandler: PartitionHandler,
        paymentPartitioner: Partitioner,
    ): Step {
        return StepBuilder("paymentManagerStep", jobRepository)
            .partitioner("paymentPartitioner", paymentPartitioner)
            .partitionHandler(partitionHandler)
            .build()
    }

    // 3. PartitionHandler 설정 (스레드 풀 및 실행 스텝 연결)
    @Bean
    fun paymentPartitionHandler(
        paymentWorkerStep: Step
    ): PartitionHandler {
        val handler = TaskExecutorPartitionHandler()
        handler.setTaskExecutor(paymentBatchTaskExecutor()) // 스레드 풀 주입
        handler.step = paymentWorkerStep
        handler.gridSize = 6 // 동시에 실행할 최대 스레드 수
        return handler
    }

    @Bean
    @JobScope
    fun paymentPartitioner(
        @Value("#{jobParameters['totalCount']}") totalCount: Int,
        @Value("#{jobParameters['stepSize']}") stepSize: Int
    ): Partitioner {
        return RangePartitioner(totalCount, stepSize)
    }

    // 4. Worker Step (Slave) 설정
    @Bean
    fun paymentWorkerStep(
        paymentReader: ItemReader<PaymentLedger>,
        paymentProcessor: ItemProcessor<PaymentLedger, PaymentLedger>,
        paymentWriter: ItemWriter<PaymentLedger>,
    ): Step {
        return StepBuilder("paymentWorkerStep", jobRepository)
            .chunk<PaymentLedger, PaymentLedger>(CHUNK_SIZE, transactionManager)
            .reader(paymentReader)
            .processor(paymentProcessor)
            .writer(paymentWriter)
            .build()
    }

    // 5. Slave에서 사용할 Reader (파티셔너가 넘겨준 날짜 활용)
    @Bean
    @StepScope
    fun paymentReader(
        @Value("#{stepExecutionContext['fromIndex']}") fromIndex: Int,
        @Value("#{stepExecutionContext['toIndex']}") toIndex: Int
    ): MongoCursorItemReader<PaymentLedger> {
        val query = Query().apply {
            addCriteria(
                Criteria.where("orderNumber").gte(fromIndex).lte(toIndex)
            )
        }
        log.info(">>> [Reader Query] 파티션 범위: $fromIndex ~ $toIndex, 쿼리내용: $query")

        return MongoCursorItemReaderBuilder<PaymentLedger>()
            .name("paymentReader")
            .template(mongoTemplate)
            .collection("payment_ledger")
            .targetType(PaymentLedger::class.java)
            .query(query)
            .sorts(mapOf("orderNumber" to Sort.Direction.ASC))
            .build()
    }

    @Bean
    @StepScope
    fun paymentBatchTaskExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 6
        executor.maxPoolSize = 10
        executor.setThreadNamePrefix("batch-thread-")
        executor.initialize()
        return executor
    }

    @Bean
    fun paymentProcessor(): ItemProcessor<PaymentLedger, PaymentLedger> = ItemProcessor { item ->
        log.info(">>> [Thread: ${Thread.currentThread().name}] Processing transaction: ${item.transactionId}")
        item.copy(updatedAt = LocalDateTime.now())
    }

    @Bean
    fun paymentWriter(): ItemWriter<PaymentLedger> = ItemWriter { items ->
        if (items.isEmpty) return@ItemWriter

        val bulkOps = mongoTemplate.bulkOps(
            BulkOperations.BulkMode.UNORDERED,
            PaymentLedger::class.java,
            "payment_ledger_backup"
        )

        bulkOps.insert(items.toList())

        val result = bulkOps.execute()
        log.info(">>> [Bulk Write] Inserted: ${result.insertedCount}, Chunk Size: ${items.size()}")
    }
}