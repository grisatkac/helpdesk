package by.tms.tkach.helpdesk.dto.task;

import by.tms.tkach.helpdesk.entities.enums.task.Urgency;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@Builder
public class TaskUpdateDTO {
    private Long id;
    private String name;
    private String description;
    private Urgency urgency;
    private String queueName;
}
