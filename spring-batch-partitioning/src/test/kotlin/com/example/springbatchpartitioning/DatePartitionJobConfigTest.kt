package com.example.springbatchpartitioning

import com.example.springbatchpartitioning.config.TestBatchConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBatchTest // 배치 테스트용 유틸리티(JobLauncherTestUtils 등)를 빈으로 등록
@SpringBootTest(classes = [DatePartitionJobConfig::class, TestBatchConfig::class]) // 테스트할 설정 클래스 로드
class DatePartitionJobConfigTest {

    @Autowired
    private lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Test
    @DisplayName("2026-01-01부터 2026-02-01까지 날짜 파티셔닝 배치가 성공적으로 실행되어야 한다")
    fun `success date partitioning job`() {
        // given: Job Parameter가 필요하다면 여기서 생성 (현재 코드엔 내부 고정이므로 생략 가능)

        // when: Job 실행
        val jobExecution = jobLauncherTestUtils.launchJob()

        // then: 1. 전체 Job 결과가 COMPLETED 인지 확인
        assertThat(jobExecution.status).isEqualTo(BatchStatus.COMPLETED)

        // then: 2. 생성된 StepExecution 확인 (Master 1개 + Slave 32일치 = 총 33개 이상)
        val stepExecutions = jobExecution.stepExecutions
        println("총 실행된 Step 수: ${stepExecutions.size}")

        // 2026-01-01 ~ 2026-02-01은 총 32일이므로, 마스터를 포함해 최소 32개 이상의 스텝이 실행되어야 함
        assertThat(stepExecutions.size).isGreaterThanOrEqualTo(32)

        // then: 3. 개별 슬레이브 스텝의 ExitStatus 확인
        stepExecutions.forEach { stepExecution ->
            assertThat(stepExecution.exitStatus.exitCode).isEqualTo("COMPLETED")
        }
    }
}