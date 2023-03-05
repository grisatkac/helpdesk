package by.tms.tkach.helpdesk.dto.taskQueue;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class TaskQueueUpdateDTO {

    private Long id;
    @NotEmpty(message = "Can't be empty")
    @Size(min = 10, message = "Name of the queue must be contain minimum 10 characters")
    private String name;
    private String description;
    @NotEmpty(message = "Department name can't be empty")
    @Size(min = 5, message = "Minimum characters of department name is 5")
    private String departmentName;
}
