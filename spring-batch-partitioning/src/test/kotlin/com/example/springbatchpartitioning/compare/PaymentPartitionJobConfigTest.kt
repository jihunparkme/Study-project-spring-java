package com.example.springbatchpartitioning.compare

import com.example.springbatchpartitioning.config.TestBatchConfig
import org.slf4j.LoggerFactory
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = [PaymentPartitionJobConfig::class, TestBatchConfig::class])
class PaymentPartitionJobConfigTest {

    @Autowired
    private lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun `success date partitioning job`() {
        val totalCount = 5_000_000L
        val jobParameters = JobParametersBuilder()
            .addLong("totalCount", totalCount)
            .addLong("stepSize", 1000)
            .toJobParameters()

        val jobExecution = jobLauncherTestUtils.launchJob(jobParameters)

        val startTime = jobExecution.startTime
        val endTime = jobExecution.endTime

        if (startTime != null && endTime != null) {
            val duration = Duration.between(startTime, endTime)
            val minutes = duration.toMinutes()
            val seconds = duration.seconds % 60

            log.info("====================================================")
            log.info("JOB STATUS: ${jobExecution.status}")
            log.info("소요 시간: ${minutes}분 ${seconds}초 (${duration.toMillis()} ms)")
            log.info("아이템당 평균 처리 속도: ${duration.toMillis().toDouble() / totalCount} ms/item")
            log.info("====================================================")
        }

        assertEquals(ExitStatus.COMPLETED, jobExecution.exitStatus)
    }
}

// TODO: 파티셔닝 적용한걸로 만들어 보기
// 5_000_000L
//     소요 시간: 8분 55초 (535238 ms)
//     아이템당 평균 처리 속도: 0.1070476 ms/item
// 1_000_000L
//     소요 시간: 1분 53초 (113469 ms)
//     아이템당 평균 처리 속도: 0.113469 ms/item
// 100_000L
//     소요 시간: 0분 15초 (15883 ms)
//     아이템당 평균 처리 속도: 0.15883 ms/item
// 10_000L
//     소요 시간: 0분 2초 (2495 ms)
//     아이템당 평균 처리 속도: 0.2495 ms/item