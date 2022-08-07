# Spring Batch

향로님 Spring Batch 가이드 보고 따라하기

> [1. Spring Batch 가이드 - 배치 어플리케이션이란?](https://jojoldu.tistory.com/324)
> [2. Spring Batch 가이드 - Batch Job 실행해보기](https://jojoldu.tistory.com/325?category=902551)
> [3. Spring Batch 가이드 - 메타테이블엿보기](https://jojoldu.tistory.com/326)
> [4. Spring Batch 가이드 - Spring Batch Job Flow](https://jojoldu.tistory.com/328)
> [5. Spring Batch 가이드 - Spring Batch Scope & Job Parameter](https://jojoldu.tistory.com/330)
> [6. Spring Batch 가이드 - Chunk 지향 처리](https://jojoldu.tistory.com/331?category=902551)
> [7. Spring Batch 가이드 - ItemReader](https://jojoldu.tistory.com/336?category=902551)
> [8. Spring Batch 가이드 - ItemWriter](https://jojoldu.tistory.com/339?category=902551)
> [9. Spring Batch 가이드 - ItemProcessor](https://jojoldu.tistory.com/347?category=902551)

> [Spring Batch](https://spring.io/projects/spring-batch#learn)

## 배치 기능 활성화

보통 실시간 처리가 어려운 대용량 데이터나 대규모 데이터일 경우 배치를 활용

- 배치기능 활성화를 위해 프로젝트 Application 클래스에 @EnableBatchProcessing 를 추가해주자.

```java
@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBatchTestApplication.class, args);
    }
}
```

## Job 생성하기

- Spring Batch의 모든 Job은 @Configuration으로 등록해서 사용
- `spring.batch.job.names: ${job.name:NONE}`: Program arguments로 job.name 값이 넘어오면 해당 값과 일치하는 Job만 실행
  - `--job.name=stepNextJob`
- 실제 운영 환경에서는 아래와 같이 배치를 실행
  - `java -jar batch-application.jar --job.name=simpleJob`
  
## DB 연동

schema~.sql 파일을 활용하여 spring batch에 필요한 테이블 생성

```yml
spring:
  datasource:
    hikari:
      jdbc-url: jdbc:h2:tcp://localhost/~/spring_batch;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
      username: sa
      password:
      driver-class-name: org.h2.Driver
```

```text
BATCH_JOB_EXECUTION
BATCH_JOB_EXECUTION_CONTEXT
BATCH_JOB_EXECUTION_PARAMS
BATCH_JOB_INSTANCE
BATCH_STEP_EXECUTION
BATCH_STEP_EXECUTION_CONTEXT
```

### BATCH_JOB_INSTANCE

Job Parameter에 따라 생성되는 테이블

- Job Parameter
  - Spring Batch가 실행될 때 외부에서 받을 수 있는 파라미터
  - Job Parameter를 사용할 경우 Program arguments에 requestDate=20220805 추가
  - Job Parameter가 같을 경우 테이블에 기록되지 않음 -> 동일한 Job Parameter는 여러개 존재할 수 없음
- Job Parameter를 사용하기 위해선 Spring Batch 전용 Scope인 `@StepScope`와 `@JobScope` 선언으로 Bean을 생성해야 함

| JOB\_INSTANCE\_ID | VERSION | JOB\_NAME | JOB\_KEY |
| :--- | :--- | :--- | :--- |
| 1 | 0 | simpleJob | d41d8cd98f00b204e9800998ecf8427e |
| 2 | 0 | simpleJob | cf8d12b1bf88779ebd68926856b6bbfc |

### BATCH_JOB_EXECUTION

JOB_EXECUTION와 JOB_INSTANCE는 부모-자식 관계

- 자신의 부모 JOB_INSTACNE가 성공/실패했던 모든 내역 저장
- 동일한 Job Parameter로 성공한 기록이 있을 때만 재수행 불가

| JOB\_EXECUTION\_ID | VERSION | JOB\_INSTANCE\_ID | CREATE\_TIME | START\_TIME | END\_TIME | STATUS | EXIT\_CODE | EXIT\_MESSAGE | LAST\_UPDATED | JOB\_CONFIGURATION\_LOCATION |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | 2 | 1 | 2022-08-05 20:59:22.693000 | 2022-08-05 20:59:22.726000 | 2022-08-05 20:59:22.795000 | COMPLETED | COMPLETED |  | 2022-08-05 20:59:22.795000 | NULL |
| 2 | 2 | 2 | 2022-08-05 21:04:21.394000 | 2022-08-05 21:04:21.451000 | 2022-08-05 21:04:21.539000 | COMPLETED | COMPLETED |  | 2022-08-05 21:04:21.539000 | NULL |
| 3 | 2 | 3 | 2022-08-05 21:09:25.507000 | 2022-08-05 21:09:25.548000 | 2022-08-05 21:09:25.658000 | FAILED | FAILED | java.lang.IllegalArgumentException: step1에서 실패합니다.<br/>at com.example.springbatchtest.job.SimpleJobConfiguration.lambda$simpleStep1$0\(SimpleJobConfiguration.java:35\)<br/>at org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction\(TaskletStep.java:407\)<br/>at org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction\(TaskletStep.java:331\)<br/>at org.springframework.transaction.support.TransactionTemplate.execute\(TransactionTemplate.java:140\)<br/>at org.springframework.batch.core.step.tasklet.TaskletStep$2.doInChunkContext\(TaskletStep.java:273\)<br/>at org.springframework.batch.core.scope.context.StepContextRepeatCallback.doInIteration\(StepContextRepeatCallback.java:82\)<br/>at org.springframework.batch.repeat.support.RepeatTemplate.getNextResult\(RepeatTemplate.java:375\)<br/>at org.springframework.batch.repeat.support.RepeatTemplate.executeInternal\(RepeatTemplate.java:215\)<br/>at org.springframework.batch.repeat.support.RepeatTemplate.iterate\(RepeatTemplate.java:145\)<br/>at org.springframework.batch.core.step.tasklet.TaskletStep.doExecute\(TaskletStep.java:258\)<br/>at org.springframework.batch.core.step.AbstractStep.execute\(AbstractStep.java:208\)<br/>at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0\(Native Method\)<br/>at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke\(NativeMethodAccessorImpl.java:62\)<br/>at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke\(DelegatingMethodAccessorImpl.java:43\)<br/>at java.base/java.lang.reflect.Method.invoke\(Method.java:566\)<br/>at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection\(AopUtils.java:344\)<br/>at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint\(ReflectiveMethodInvocation.java:198\)<br/>at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed\(ReflectiveMethodInvocation.java:163\)<br/>at org.springframework.aop.support.DelegatingIntroductionInterceptor.doProceed\(DelegatingIntroductionInterceptor.java:137\)<br/>at org.springframework.aop.support.DelegatingIntroductionInterceptor.invoke\(DelegatingIntroductionInterceptor.java:124\)<br/>at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed\(ReflectiveMethodInvocation.java:186\)<br/>at org.springframework.aop.framework.JdkDynamicAopProxy.invoke\(JdkDynamicAopProxy.java:215\)<br/>at com.sun.proxy.$Proxy71.execute\(Unknown Source\)<br/>at org.springframework.batch.core.job.SimpleStepHandler.handleStep\(SimpleStepHandler.java:152\)<br/>at org.springframewo | 2022-08-05 21:09:25.658000 | NULL |
| 4 | 2 | 3 | 2022-08-05 21:11:00.480000 | 2022-08-05 21:11:00.521000 | 2022-08-05 21:11:00.700000 | COMPLETED | COMPLETED |  | 2022-08-05 21:11:00.701000 | NULL |

### BATCH_JOB_EXECUTION_PARAM

BATCH_JOB_EXECUTION 테이블이 생성될 당시에 입력 받은 Job Parameter를 저장

| JOB\_EXECUTION\_ID | TYPE\_CD | KEY\_NAME | STRING\_VAL | DATE\_VAL | LONG\_VAL | DOUBLE\_VAL | IDENTIFYING |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 2 | STRING | requestDate | 20220805 | 1970-01-01 09:00:00.000000 | 0 | 0 | Y |
| 3 | STRING | requestDate | 20220806 | 1970-01-01 09:00:00.000000 | 0 | 0 | Y |
| 4 | STRING | requestDate | 20220806 | 1970-01-01 09:00:00.000000 | 0 | 0 | Y |


## Job 구성

### Step

실제 Batch 작업을 수행하는 역할

- Batch로 실제 처리하고자 하는 기능과 설정을 모두 포함

```java
@Slf4j //=> log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor //=> 생성자 DI를 위한 lombok 어노테이션
@Configuration //=> Spring Batch의 모든 Job은 @Configuration을 등록해서 사용
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob") //=> simpleJob 이란 이름의 Batch Job 생성
                .start(simpleStep1(null))
                .next(simpleStep2(null))
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
//                    throw new IllegalArgumentException("step1에서 실패합니다.");
                })
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step2");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
```

### Next

Step들간에 순서 혹은 처리 흐름을 제어

```java
@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job stepNextJob() {
    return jobBuilderFactory.get("stepNextJob")
            .start(step1())
            .next(step2())
            .next(step3())
            .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
            .tasklet((contribution, chunkContext) -> {
              log.info(">>>>> This is Step1");
              return RepeatStatus.FINISHED;
            })
            .build();
  }

  @Bean
  public Step step2() {
    return stepBuilderFactory.get("step2")
            .tasklet((contribution, chunkContext) -> {
              log.info(">>>>> This is Step2");
              return RepeatStatus.FINISHED;
            })
            .build();
  }

  @Bean
  public Step step3() {
    return stepBuilderFactory.get("step3")
            .tasklet((contribution, chunkContext) -> {
              log.info(">>>>> This is Step3");
              return RepeatStatus.FINISHED;
            })
            .build();
  }
}
```

### Flow

조건별 흐름 제어

- 상황에 따라 정상일때는 Step B로, 오류가 났을때는 Step C로 수행할 경우 활용

```java
@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextConditionalJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * step1 실패 시나리오: step1 -> step3
     * step1 성공 시나리오: step1 -> step2 -> step3
     */
    @Bean
    public Job stepNextConditionalJob() {
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobStep1())
                    .on("FAILED") // FAILED 일 경우 (캐치할 ExitStatus 지정, * 일 경우 모든 ExitStatus가 지정)
                    .to(conditionalJobStep3()) // step3으로 이동한다. (다음으로 이동할 Step 지정)
                    .on("*") // step3의 결과 관계 없이
                    .end() // step3으로 이동하면 Flow가 종료한다.
                .from(conditionalJobStep1()) // step1로부터 (상태값을 보고 일치하는 상태라면 to()에 포함된 step을 호출)
                    .on("*") // FAILED 외에 모든 경우
                    .to(conditionalJobStep2()) // step2로 이동한다.
                    .next(conditionalJobStep3()) // step2가 정상 종료되면 step3으로 이동한다.
                    .on("*") // step3의 결과 관계 없이
                    .end() // step3으로 이동하면 Flow가 종료한다. (FlowBuilder를 반환하는 end)
                .end() // Job 종료 (FlowBuilder를 종료하는 end)
                .build();
    }

    @Bean
    public Step conditionalJobStep1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step1");

                    /**
                     ExitStatus를 FAILED로 지정한다.
                     해당 status를 보고 flow가 진행된다.
                     **/
//                    contribution.setExitStatus(ExitStatus.FAILED);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalJobStep2() {
        return stepBuilderFactory.get("conditionalJobStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalJobStep3() {
        return stepBuilderFactory.get("conditionalJobStep3")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
```

### Decide

분기 처리

- Step과는 명확히 역할과 책임을 분리
- Step들의 Flow속에서 분기만 담당하는 타입 (JobExecutionDecider)

```java
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeciderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job deciderJob() {
        return jobBuilderFactory.get("deciderJob")
                .start(startStep())// Job Flow의 첫번째 Step을 시작
                .next(decider()) // 홀수 | 짝수 구분 (startStep 이후에 decider를 실행)
                .from(decider()) // decider의 상태가 (decider의 상태값을 보고 일치하는 상태라면 to()에 포함된 step 호출)
                    .on("ODD") // ODD라면
                    .to(oddStep()) // oddStep로 간다.
                .from(decider()) // decider의 상태가
                    .on("EVEN") // ODD라면
                    .to(evenStep()) // evenStep로 간다.
                .end() // builder 종료
                .build();
    }

    @Bean
    public Step startStep() {
        return stepBuilderFactory.get("startStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> Start!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step evenStep() {
        return stepBuilderFactory.get("evenStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> 짝수입니다.");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step oddStep() {
        return stepBuilderFactory.get("oddStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> 홀수입니다.");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new OddDecider();
    }

    public static class OddDecider implements JobExecutionDecider {

        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            Random rand = new Random();

            int randomNumber = rand.nextInt(50) + 1;
            log.info("랜덤숫자: {}", randomNumber);

            if(randomNumber % 2 == 0) {
                return new FlowExecutionStatus("EVEN");
            } else {
                return new FlowExecutionStatus("ODD");
            }
        }
    }
}
```

### Chunk

데이터 덩어리로 작업 할 때 각 커밋 사이에 처리되는 row 수

- Chunk 지향 처리: 한 번에 하나씩 데이터를 읽어 Chunk라는 덩어리를 만든 뒤, Chunk 단위로 트랜잭션을 다루는 것
- 실패할 경우엔 해당 Chunk 만큼만 롤백

**Chunk Size VS Page Size**

- Chunk Size는 한번에 처리될 트랜잭션 단위를 얘기하며, Page Size는 한번에 조회(Page 단위로 끊어서 조회)할 Item의 양
- 성능 이슈와 영속성 컨텍스트의 깨지는 문제를 막기 위해 두 개 값을 일치시키는 것이 좋음

**동작 `ChunkOrientedTasklet`**

ItemReader, ItemWriter는 ChunkOrientedTasklet에서 필수 요소

- `ItemReader`: Reader에서 데이터를 읽고, chunk size만큼 데이터를 누적, 처리가 필요할 경우 ItemProcessor에 전달
  - 가장 큰 장점은 데이터를 Streaming이 가능하다는 것
  - read() 메소드는 데이터를 하나씩 가져와 ItemWriter로 데이터를 전달하고, 다음 데이터를 다시 가져 온다. 
  - 이를 통해 reader & processor & writer가 Chunk 단위로 수행되고 주기적으로 Commit
- `ItemProcessor`:  청크의 Item 개수 만큼 처리 될 때까지 가공
- `ItemWriter`: 가공된 데이터들을 별도의 공간에 모은 뒤, Chunk 단위만큼 쌓이게 되면 Writer에 전달하고 Writer는 일괄 처리 

## ItemReader

- JpaRepository를 ListItemReader, QueueItemReader에 사용하고 싶을 경우, RepositoryItemReader 사용하기
- 영속성 컨텍스트가 필요한 Reader 사용시 fetchSize와 ChunkSize는 같은 값을 유지
- PagingItemReader 사용 시 정렬 포함 필수

### **CursorItemReader**
- Database와 SocketTimeout을 충분히 큰 값으로 설정 필요
- Cursor는 하나의 Connection으로 Batch가 끝날때까지 사용되므로 중간에 끊어질 수 있음

```java
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcCursorItemReaderJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource; // DataSource DI

    private static final int chunkSize = 10;

    @Bean
    public Job jdbcCursorItemReaderJob() {
        return jobBuilderFactory.get("jdbcCursorItemReaderJob")
                .start(jdbcCursorItemReaderStep())
                .build();
    }

    @Bean
    public Step jdbcCursorItemReaderStep() {
        return stepBuilderFactory.get("jdbcCursorItemReaderStep")
                .<Pay, Pay>chunk(chunkSize) // <Reader에서 반환할 타입, Writer에 파라미터로 넘어올 타입> ()
                .reader(jdbcCursorItemReader())
                .writer(jdbcCursorItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Pay> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Pay>()
                .fetchSize(chunkSize) // Database에서 한번에 가져올 데이터 양
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class)) // 쿼리 결과를 Java 인스턴스로 매핑하기 위한 Mapper
                .sql("SELECT id, amount, tx_name, tx_date_time FROM pay") // Reader로 사용할 쿼리문
                .name("jdbcCursorItemReader") // reader의 이름
                .build();
    }

    private ItemWriter<Pay> jdbcCursorItemWriter() {
        return list -> {
            for (Pay pay: list) {
                log.info("Current Pay={}", pay);
            }
        };
    }
}
```

### **PagingItemReader**
- Batch 수행 시간이 오래 걸리는 경우 CursorItemReader 대신 사용
- 한 페이지를 읽을때마다 Connection을 맺고 끊기 때문에 많은 데이터라도 타임아웃과 부하 없이 수행 가능

```java
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcPagingItemReaderJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource; // DataSource DI

    private static final int chunkSize = 10;

    @Bean
    public Job jdbcPagingItemReaderJob() throws Exception {
        return jobBuilderFactory.get("jdbcPagingItemReaderJob")
                .start(jdbcPagingItemReaderStep())
                .build();
    }

    @Bean
    public Step jdbcPagingItemReaderStep() throws Exception {
        return stepBuilderFactory.get("jdbcPagingItemReaderStep")
                .<Pay, Pay>chunk(chunkSize)
                .reader(jdbcPagingItemReader())
                .writer(jdbcPagingItemWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Pay> jdbcPagingItemReader() throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("amount", 2000);

        return new JdbcPagingItemReaderBuilder<Pay>()
                .pageSize(chunkSize)
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class))
                .queryProvider(createQueryProvider()) // JdbcCursorItemReader와 다른 부분
                .parameterValues(parameterValues) // 쿼리에 대한 매개 변수 값의 Map을 지정 (where 절에서 선언된 파라미터 변수)
                .name("jdbcPagingItemReader")
                .build();
    }

    private ItemWriter<Pay> jdbcPagingItemWriter() {
        return list -> {
            for (Pay pay: list) {
                log.info("Current Pay={}", pay);
            }
        };
    }

    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource); // Database에 맞는 PagingQueryProvider를 선택하기 위해
        queryProvider.setSelectClause("id, amount, tx_name, tx_date_time");
        queryProvider.setFromClause("from pay");
        queryProvider.setWhereClause("where amount >= :amount");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }
}
```

### **JpaPagingItemReader**

- Querydsl, Jooq 등을 통한 ItemReader 구현체는 공식 지원되지 않으므로 CustomItemReader 구현 필요
- HibernatePagingItemReader 에서는 Cursor 지원이 되지만 JpaPagingItemReader에서는 Cursor 기반 Database 접근을 지원하지 않음
- [Creating Custom ItemReaders and ItemWriters](https://docs.spring.io/spring-batch/docs/current/reference/html/readersAndWriters.html#customReadersWriters)

```java
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

    @Bean
    public Job jpaPagingItemReaderJob() {
        return jobBuilderFactory.get("jpaPagingItemReaderJob")
                .start(jpaPagingItemReaderStep())
                .build();
    }

    @Bean
    public Step jpaPagingItemReaderStep() {
        return stepBuilderFactory.get("jpaPagingItemReaderStep")
                .<Pay, Pay>chunk(chunkSize)
                .reader(jpaPagingItemReader())
                .writer(jpaPagingItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Pay> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<Pay>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM Pay p WHERE amount >= 2000")
                .build();
    }

    private ItemWriter<Pay> jpaPagingItemWriter() {
        return list -> {
            for (Pay pay: list) {
                log.info("Current Pay={}", pay);
            }
        };
    }
}
```

## ItemWriter

Reader, Prcessor와 함께 ChunkOrientedTasklet을 구성하는 3 요소

### **JdbcBatchItemWriter**

- ORM을 사용하지 않는 경우 대부분 사용
- 성능 향상을 위해 ChunkSize만큼 쿼리를 모으고, 모아놓은 쿼리를 DB에 전송하여 한 번에 실행 

```java
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcBatchItemWriterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource; // DataSource DI

    private static final int chunkSize = 10;

    @Bean
    public Job jdbcBatchItemWriterJob() {
        return jobBuilderFactory.get("jdbcBatchItemWriterJob")
                .start(jdbcBatchItemWriterStep())
                .build();
    }

    @Bean
    public Step jdbcBatchItemWriterStep() {
        return stepBuilderFactory.get("jdbcBatchItemWriterStep")
                .<Pay, Pay>chunk(chunkSize)
                .reader(jdbcBatchItemWriterReader())
                .writer(jdbcBatchItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Pay> jdbcBatchItemWriterReader() {
        return new JdbcCursorItemReaderBuilder<Pay>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class))
                .sql("SELECT id, amount, tx_name, tx_date_time FROM pay")
                .name("jdbcBatchItemWriter")
                .build();
    }

    /**
     * reader에서 넘어온 데이터를 하나씩 출력하는 writer
     */
    @Bean // beanMapped()을 사용할때는 필수
    public JdbcBatchItemWriter<Pay> jdbcBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<Pay>()
                .dataSource(dataSource)
                .sql("insert into pay2(amount, tx_name, tx_date_time) values (:amount, :txName, :txDateTime)")
                .beanMapped() // Pojo 기반으로 Insert SQL의 Values를 매핑
                .build();
    }
}

```

### **JpaItemWriter**

- Writer에 전달하는 데이터가 Entity 클래스일 경우 사용
- JPA를 사용하기 때문에 영속성 관리를 위해 EntityManager를 할당

```java
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaItemWriterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int chunkSize = 10;

    @Bean
    public Job jpaItemWriterJob() {
        return jobBuilderFactory.get("jpaItemWriterJob")
                .start(jpaItemWriterStep())
                .build();
    }

    @Bean
    public Step jpaItemWriterStep() {
        return stepBuilderFactory.get("jpaItemWriterStep")
                .<Pay, Pay2>chunk(chunkSize)
                .reader(jpaItemWriterReader())
                .processor(jpaItemProcessor())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Pay> jpaItemWriterReader() {
        return new JpaPagingItemReaderBuilder<Pay>()
                .name("jpaItemWriterReader")
                .entityManagerFactory(entityManagerFactory) // 영속성 관리를 위한 할당
                .pageSize(chunkSize)
                .queryString("SELECT p FROM Pay p")
                .build();
    }

    @Bean
    public ItemProcessor<Pay, Pay2> jpaItemProcessor() {
        return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
    }

    @Bean
    public JpaItemWriter<Pay2> jpaItemWriter() {
        JpaItemWriter<Pay2> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
```

### **Custom ItemWriter**

ItemWriter Interface 구현

- Querydsl/Jooq 기반의 ItemReader, Custom 하게 구현이 필요한 경우 사용
  - Reader에서 읽어온 데이터를 RestTemplate으로 외부 API로 전달해야할 경우
  - 임시저장을 하고 비교하기 위해 싱글톤 객체에 값을 넣어야할 경우 
  - 여러 Entity를 동시에 save 해야할 경우
  - ...

[Spring Batch ItemWriter에 List 전달하기](https://jojoldu.tistory.com/140)

```java
@Slf4j
@RequiredArgsConstructor
@Configuration
public class CustomItemWriterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int chunkSize = 10;

    @Bean
    public Job customItemWriterJob() {
        return jobBuilderFactory.get("customItemWriterJob")
                .start(customItemWriterStep())
                .build();
    }

    @Bean
    public Step customItemWriterStep() {
        return stepBuilderFactory.get("customItemWriterStep")
                .<Pay, Pay2>chunk(chunkSize)
                .reader(customItemWriterReader())
                .processor(customItemWriterProcessor())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Pay> customItemWriterReader() {
        return new JpaPagingItemReaderBuilder<Pay>()
                .name("customItemWriterReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM Pay p")
                .build();
    }

    @Bean
    public ItemProcessor<Pay, Pay2> customItemWriterProcessor() {
        return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
    }

    @Bean
    public ItemWriter<Pay2> customItemWriter() {
        return items -> { // write() @Override 하면 구현체 생성
            for (Pay2 item : items) {
                System.out.println(item);
            }
        };
    }
}
```

## ItemProcessor

**Reader에서 넘겨준 데이터 개별건을 가공/처리하는 역할**

`public interface ItemProcessor<I, O>`

- ItemProcessor는 선택사항 (Writer 부분에서도 충분히 구현 가능)
- 각 계층(읽기/처리/쓰기)을 분리할 수 있는 좋은 대안
- 보통 비즈니스 로직을 담당
  - 변환 
    - Reader에서 읽은 데이터를 원하는 타입으로 변환해서 Writer에 넘기기 
  - 필터 
    - Reader에서 넘겨준 데이터를 Writer로 넘겨줄 것인지를 결정 
    - null 반환 시 Writer에 전달하지 않음

### 변환

Reader에서 읽은 타입을 변환하여 Writer에 전달

```java
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ProcessorConvertJobConfiguration {

    public static final String JOB_NAME = "ProcessorConvertBatch";
    public static final String BEAN_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    @Value("${chunkSize:1000}")
    private int chunkSize;

    @Bean(JOB_NAME)
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .preventRestart()
                .start(step())
                .build();
    }

    @Bean(BEAN_PREFIX + "step")
    @JobScope
    public Step step() {
        return stepBuilderFactory.get(BEAN_PREFIX + "step")
                .<Teacher, String>chunk(chunkSize) // ChunkSize 앞에 선언될 타입 역시 Reader와 Writer 타입과 동일하게
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Teacher> reader() {
        return new JpaPagingItemReaderBuilder<Teacher>()
                .name(BEAN_PREFIX+"reader")
                .entityManagerFactory(emf)
                .pageSize(chunkSize)
                .queryString("SELECT t FROM Teacher t")
                .build();
    }

    /**
     * public interface ItemProcessor<I, O>
     * I : Reader에서 읽어올 타입이
     * O : Writer에서 넘겨줄 타입
     */
    @Bean
    public ItemProcessor<Teacher, String> processor() {
        return teacher -> {
            return teacher.getName();
        };
    }

    private ItemWriter<String> writer() {
        return items -> {
            for (String item : items) {
                log.info("Teacher Name= {}", item);
            }
        };
    }
}
```

### 필터

Writer에 값을 넘길지 말지를 Processor에서 판단

```java
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ProcessorNullJobConfiguration {

    public static final String JOB_NAME = "processorNullBatch";
    public static final String BEAN_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    @Value("${chunkSize:1000}")
    private int chunkSize;

    @Bean(JOB_NAME)
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .preventRestart()
                .start(step())
                .build();
    }

    @Bean(BEAN_PREFIX + "step")
    @JobScope
    public Step step() {
        return stepBuilderFactory.get(BEAN_PREFIX + "step")
                .<Teacher, Teacher>chunk(chunkSize)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Teacher> reader() {
        return new JpaPagingItemReaderBuilder<Teacher>()
                .name(BEAN_PREFIX+"reader")
                .entityManagerFactory(emf)
                .pageSize(chunkSize)
                .queryString("SELECT t FROM Teacher t")
                .build();
    }

    @Bean
    public ItemProcessor<Teacher, Teacher> processor() {
        return teacher -> {

            boolean isIgnoreTarget = teacher.getId() % 2 == 0L;
            if(isIgnoreTarget){
                log.info(">>>>>>>>> ID={}, Teacher name={}, isIgnoreTarget={}", teacher.getId(), teacher.getName(), isIgnoreTarget);
                return null; // null 반환 시 Writer에 전달하지 않음
            }

            return teacher;
        };
    }

    private ItemWriter<Teacher> writer() {
        return items -> {
            for (Teacher item : items) {
                log.info("DI= {}, Teacher Name={}", item.getId(), item.getName());
            }
        };
    }
}
```

### CompositeItemProcessor

ItemProcessor간의 체이닝을 지원하는 Processor

- 변환이 2번 이상으로 필요할 경우

## 트랜잭션 범위

트랜잭션 범위는 Chunk단위

- Reader에서 Entity를 반환해 주었다면 Entity간의 Lazy Loading 가능

### Processor

- Processor는 트랜잭션 범위 안이며, Entity의 Lazy Loading 가능

### Writer

- Writer도 마찬가지로 트랜잭션 범위 안이며, Entity의 Lazy Loading 가능 