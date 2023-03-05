package by.tms.tkach.helpdesk.dto.task.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class TaskCreateRequestDTO {
    @NotEmpty(message = "Can't be empty")
    @Size(min = 10, max = 100, message = "Name of the task must contain from 10 to 100 characters")
    private String name;
    @Size(max = 1000)
    private String description;
    private LocalDate createdDate;
    private String status;
    private String urgency;
    @NotEmpty(message = "Can't be empty")
    @Size(min = 10, message = "Task queue name must contain minimum 10 characters")
    private String taskQueueName;
}
