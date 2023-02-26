package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.dto.task.response.TaskDetailsResponseDTO;
import by.tms.tkach.helpdesk.dto.task.response.TaskUpdateResponseDTO;
import by.tms.tkach.helpdesk.dto.user.TaskPagingResponseDTO;
import by.tms.tkach.helpdesk.entities.Task;
import by.tms.tkach.helpdesk.entities.User;

public interface TaskService extends CrudService<Task> {

    TaskDetailsResponseDTO findTaskDetails(Long id);

    TaskUpdateResponseDTO getForUpdate(Long id);

    TaskPagingResponseDTO getAllPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    TaskPagingResponseDTO getAllPageableByFilter(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String filter, String requestURI);

    boolean completeTask(Long id);
    boolean takeTask(Long id, User user);
}
