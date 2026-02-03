package com.example.springbatchpartitioning

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
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.support.ListItemReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Configuration
class DatePartitionJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager
) {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val CHUNK_SIZE = 1000
        private const val GRID_SIZE = 6
    }

    // 1. Job 설정
    @Bean
    fun datePartitionJob(managerStep: Step): Job =
        JobBuilder("datePartitionJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(managerStep)
            .build()

    // 2. Manager Step (Master)
    @Bean
    fun managerStep(
        partitionHandler: PartitionHandler,
        partitioner: Partitioner,
    ): Step = StepBuilder("managerStep", jobRepository)
        .partitioner("workerStep", partitioner)
        .partitionHandler(partitionHandler)
        .build()

    // 3. PartitionHandler (apply 범위 함수 활용)
    @Bean
    fun partitionHandler(workerStep: Step): PartitionHandler =
        TaskExecutorPartitionHandler().apply {
            setTaskExecutor(batchTaskExecutor())
            step = workerStep
            gridSize = GRID_SIZE
        }

    @Bean
    @JobScope
    fun partitioner(
        @Value("#{jobParameters['startDate']}") startDate: String,
        @Value("#{jobParameters['endDate']}") endDate: String
    ): Partitioner {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        return DateRangePartitioner(
            startDate = LocalDate.parse(startDate, formatter),
            endDate = LocalDate.parse(endDate, formatter)
        )
    }

    // 4. Worker Step (Slave)
    @Bean
    fun workerStep(
        reader: ItemReader<String>,
        writer: ItemWriter<String>
    ): Step = StepBuilder("workerStep", jobRepository)
        .chunk<String, String>(CHUNK_SIZE, transactionManager)
        .reader(reader)
        .writer(writer)
        .build()

    // 5. Reader
    @Bean
    @StepScope
    fun reader(
        @Value("#{stepExecutionContext['targetDate']}") targetDate: String
    ): ItemReader<String> {
        log.info(">>> [${Thread.currentThread().name}] Reading: $targetDate")
        return ListItemReader(listOf("Data for $targetDate"))
    }

    // 6. TaskExecutor (apply 활용)
    @Bean
    @StepScope
    fun batchTaskExecutor(): TaskExecutor =
        ThreadPoolTaskExecutor().apply {
            corePoolSize = GRID_SIZE
            maxPoolSize = 10
            setThreadNamePrefix("batch-thread-")
            initialize()
        }

    @Bean
    fun writer(): ItemWriter<String> = ItemWriter { items ->
        items.forEach { log.info("<<< [${Thread.currentThread().name}] saved: $it") }
    }
}