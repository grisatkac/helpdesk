package by.tms.tkach.helpdesk.dto.task.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskCreateRequestDTO {
    private String name;
    private String description;
    private LocalDate createdDate;
    private String status;
    private String urgency;
    private String taskQueueName;
}
