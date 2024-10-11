package io.spring.billsetuptask.listener

import org.springframework.cloud.task.listener.TaskExecutionListener
import org.springframework.cloud.task.repository.TaskExecution
import org.springframework.stereotype.Component

@Component
class CustomTaskListener : TaskExecutionListener {

    override fun onTaskStartup(taskExecution: TaskExecution) {
        // 작업 시작 시 실행할 코드
    }

    override fun onTaskEnd(taskExecution: TaskExecution) {
        // 작업 종료 시 EXIT_CODE와 EXIT_MESSAGE 설정
        taskExecution.exitCode = 200
        taskExecution.exitMessage = "Custom Exit Message"
    }

    override fun onTaskFailed(taskExecution: TaskExecution, throwable: Throwable) {
        // 작업이 실패했을 때 실행할 코드
        taskExecution.exitCode = -1
        taskExecution.exitMessage = "Task failed due to: ${throwable.message}"
    }
}