package com.example.springbatchpartitioning

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.partition.PartitionHandler
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

@Configuration
class DatePartitionJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager
) {

    // 1. Job 설정
    @Bean
    fun datePartitionJob(masterStep: Step): Job {
        return JobBuilder("datePartitionJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(masterStep)
            .build()
    }

    // 2. Manager Step (Master) 설정
    @Bean
    fun masterStep(partitionHandler: PartitionHandler): Step {
        return StepBuilder("masterStep", jobRepository)
            .partitioner("workerStep", DateRangePartitioner(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 2, 1)
            ))
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

    // 4. Worker Step (Slave) 설정
    @Bean
    fun workerStep(
        reader: ItemReader<String>,
        writer: ItemWriter<String>
    ): Step {
        return StepBuilder("workerStep", jobRepository)
            .chunk<String, String>(1000, transactionManager) // 1,000건 단위 청크 처리
            .reader(reader)
            .writer(writer)
            .build()
    }

    // 5. Slave에서 사용할 Reader (파티셔너가 넘겨준 날짜 활용)
    @Bean
    @StepScope
    fun reader(
        @Value("#{stepExecutionContext['targetDate']}") targetDate: String
    ): ItemReader<String> {
        // 실제로는 이 targetDate를 쿼리 조건으로 사용하여 데이터를 읽어옴
        println(">>> [Thread: ${Thread.currentThread().name}] $targetDate 데이터 읽기 시작")
        return ListItemReader(listOf("Data for $targetDate"))
    }

    @Bean
    fun batchTaskExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 6
        executor.maxPoolSize = 10
        executor.setThreadNamePrefix("batch-thread-")
        executor.initialize()
        return executor
    }

    @Bean
    fun writer(): ItemWriter<String> {
        return ItemWriter { items ->
            items.forEach { println("<<< [Thread: ${Thread.currentThread().name}] 저장 완료: $it") }
        }
    }
}