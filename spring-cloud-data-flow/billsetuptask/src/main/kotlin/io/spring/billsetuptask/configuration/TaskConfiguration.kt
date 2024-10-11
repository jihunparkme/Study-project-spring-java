package io.spring.billsetuptask.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.cloud.task.configuration.EnableTask
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

/**
 * Spring Cloud Task를 사용해 BILL_STATEMENTS 테이블을 생성
 *
 * @EnableTask : Task 실행에 관한 정보(Task 시작/종료 시간과 종료 코드 등)를 저장하는 TaskRepository 설정
 */
@Configuration
@EnableTask
class TaskConfiguration {
    @Autowired
    private val dataSource: DataSource? = null

    @Bean
    fun commandLineRunner(): CommandLineRunner {
        return CommandLineRunner { args: Array<String?>? ->
            val jdbcTemplate = JdbcTemplate(dataSource!!)
            jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS " +
                        "BILL_STATEMENTS ( " +
                            "id int, " +
                            "first_name varchar(50)," +
                            "last_name varchar(50), " +
                            "minutes int," +
                            "data_usage int, " +
                            "bill_amount double" +
                        ")"
            )
        }
    }
}

