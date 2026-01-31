package com.example.springbatchpartitioning.compare

import com.example.springbatchpartitioning.DateRangePartitioner
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
import org.springframework.batch.item.data.MongoItemWriter
import org.springframework.batch.item.data.builder.MongoCursorItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    fun datePartitionJob(managerStep: Step): Job {
        return JobBuilder("datePartitionJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(managerStep)
            .build()
    }

    // 2. Manager Step (Master) 설정
    @Bean
    fun managerStep(
        partitionHandler: PartitionHandler,
        partitioner: Partitioner,
    ): Step {
        return StepBuilder("managerStep", jobRepository)
            .partitioner("workerStep", partitioner)
            .partitionHandler(partitionHandler)
            .build()
    }

    // 3. PartitionHandler 설정 (스레드 풀 및 실행 스텝 연결)
    @Bean
    fun partitionHandler(workerStep: Step): PartitionHandler {
        val handler = TaskExecutorPartitionHandler()
        handler.setTaskExecutor(batchTaskExecutor()) // 스레드 풀 주입
        handler.step = workerStep
        handler.gridSize = 6 // 동시에 실행할 최대 스레드 수
        return handler
    }

    @Bean
    @JobScope
    fun partitioner(
        @Value("#{jobParameters['startDate']}") startDate: String,
        @Value("#{jobParameters['endDate']}") endDate: String
    ): Partitioner {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val start = LocalDate.parse(startDate, formatter)
        val end = LocalDate.parse(endDate, formatter)

        return DateRangePartitioner(start, end)
    }

    // 4. Worker Step (Slave) 설정
    @Bean
    fun workerStep(
        reader: ItemReader<PaymentLedger>,
        processor: ItemProcessor<PaymentLedger, PaymentLedger>,
        writer: ItemWriter<PaymentLedger>,
    ): Step {
        return StepBuilder("workerStep", jobRepository)
            .chunk<PaymentLedger, PaymentLedger>(CHUNK_SIZE, transactionManager) // 1,000건 단위 청크 처리
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build()
    }

    // 5. Slave에서 사용할 Reader (파티셔너가 넘겨준 날짜 활용)
    @Bean
    @StepScope
    fun reader(): MongoCursorItemReader<PaymentLedger> =
        MongoCursorItemReaderBuilder<PaymentLedger>()
            .name("cursorReader")
            .template(mongoTemplate)
            .collection("payment_ledger")
            .targetType(PaymentLedger::class.java)
            .queryString("{}")
            .sorts(mapOf("_id" to Sort.Direction.ASC))
            .build()

    @Bean
    @StepScope
    fun batchTaskExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 6
        executor.maxPoolSize = 10
        executor.setThreadNamePrefix("batch-thread-")
        executor.initialize()
        return executor
    }

    @Bean
    fun processor(): ItemProcessor<PaymentLedger, PaymentLedger> = ItemProcessor { item ->
        Thread.sleep(1000)
        log.info("Processing item: ${item.transactionId}")
        item.copy(updatedAt = LocalDateTime.now())
    }

    @Bean
    fun writer(): MongoItemWriter<PaymentLedger> =
        MongoItemWriter<PaymentLedger>().apply {
            setTemplate(mongoTemplate)
            setCollection("payment_ledger_backup")
        }
}