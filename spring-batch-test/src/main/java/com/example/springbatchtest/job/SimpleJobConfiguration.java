package com.example.springbatchtest.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j //=> log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor //=> 생성자 DI를 위한 lombok 어노테이션
@Configuration //=> Spring Batch의 모든 Job은 @Configuration을 등록해서 사용
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob") //=> simpleJob 이란 이름의 Batch Job 생성
                .start(simpleStep1())
                .build();
    }

    @Bean
    public Step simpleStep1() {
        return stepBuilderFactory.get("simpleStep1") //=> simpleStep1 이란 이름의 Batch Step 생성
                .tasklet((contribution, chunkContext) -> { //=> Step 안에서 수행될 기능들을 명시 (Step안에서 단일로 수행될 커스텀한 기능들을 선언할때  사용)
                    log.info(">>>>> This is Step1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
