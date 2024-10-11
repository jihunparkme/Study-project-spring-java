package io.spring.billsetuptask.configuration

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource
import kotlin.test.Test


@SpringBootTest
internal class BillSetupTaskApplicationTests {

    @Autowired
    private val dataSource: DataSource? = null

    @Test
    fun testRepository() {
        val jdbcTemplate = JdbcTemplate(this.dataSource!!)
        val result = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM BILL_STATEMENTS", Int::class.java
        )!!
        assertThat(result).isEqualTo(0)
    }
}