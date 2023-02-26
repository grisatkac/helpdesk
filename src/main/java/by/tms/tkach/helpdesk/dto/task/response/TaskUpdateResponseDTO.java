package by.tms.tkach.helpdesk.dto.task.response;

import by.tms.tkach.helpdesk.dto.form.DefaultDataTaskInputFields;
import by.tms.tkach.helpdesk.dto.task.TaskUpdateDTO;
import by.tms.tkach.helpdesk.entities.enums.task.Urgency;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@Builder
public class TaskUpdateResponseDTO {
    //private Long id;
    //private String name;
    //private String description;
    //private Urgency urgency;
    //private String queueName;
    private TaskUpdateDTO taskUpdate;
    private DefaultDataTaskInputFields data;
}
