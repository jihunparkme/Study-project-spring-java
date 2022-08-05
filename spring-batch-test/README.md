# Spring Batch

## 배치 기능 활성화

배치기능 활성화를 위해 프로젝트 Application 클래스에 @EnableBatchProcessing 를 추가해주자.

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

> [2. Spring Batch 가이드 - Batch Job 실행해보기](https://jojoldu.tistory.com/325?category=902551)

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
