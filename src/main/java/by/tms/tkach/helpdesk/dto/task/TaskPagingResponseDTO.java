package by.tms.tkach.helpdesk.dto.task;

import by.tms.tkach.helpdesk.dto.department.PagingDTO;
import by.tms.tkach.helpdesk.dto.task.response.TaskDetailsResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class TaskPagingResponseDTO extends PagingDTO<TaskDetailsResponseDTO> {
}
