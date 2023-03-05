package by.tms.tkach.helpdesk.dto.taskQueue.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class TaskQueueCreateRequestDTO {
    @NotEmpty(message = "Can't be empty")
    @Size(min = 10, message = "Name of the queue must be contain minimum 10 characters")
    private String name;
    @Size(max = 1000, message = "Name of the queue must be contain maximum 1000 characters")
    private String description;
}
