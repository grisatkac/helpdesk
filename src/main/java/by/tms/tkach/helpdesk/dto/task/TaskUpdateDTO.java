package by.tms.tkach.helpdesk.dto.task;

import by.tms.tkach.helpdesk.dto.form.DefaultDataTaskInputFields;
import by.tms.tkach.helpdesk.entities.enums.task.Status;
import by.tms.tkach.helpdesk.entities.enums.task.Urgency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDTO {
    private Long id;
    private String name;
    private String description;
    private Urgency urgency;
    private Status status;
    private String queueName;
    private DefaultDataTaskInputFields data;
}
