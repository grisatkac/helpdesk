package by.tms.tkach.helpdesk.dto.task.response;

import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueueShortResponseDTO;
import by.tms.tkach.helpdesk.dto.user.response.UserShortResponseDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDetailsResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate createdDate;
    private String urgency;
    private String status;
    private UserShortResponseDTO userOwner;
    private UserShortResponseDTO userExecutor;
    private TaskQueueShortResponseDTO queue;
}
