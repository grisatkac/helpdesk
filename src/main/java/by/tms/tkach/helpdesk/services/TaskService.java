package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.dto.task.TaskUpdateDTO;
import by.tms.tkach.helpdesk.dto.task.response.TaskDetailsResponseDTO;
import by.tms.tkach.helpdesk.dto.task.response.TaskUpdateResponseDTO;
import by.tms.tkach.helpdesk.dto.task.TaskPagingResponseDTO;
import by.tms.tkach.helpdesk.entities.Task;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.entities.enums.task.Status;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TaskService extends CrudService<Task> {

    TaskDetailsResponseDTO findTaskDetails(Long id);

    TaskUpdateDTO getForUpdate(Long id);

    TaskPagingResponseDTO getAllPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String requestURI);

    TaskPagingResponseDTO getAllPageableByFilter(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String filter, String requestURI);

    Optional<Task> getTaskWithQueue(Long ig);

    Page<Task> getPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String filter);

    //boolean completeTask(Long id);

    //boolean takeTask(Long id, User user);

    String validateTaskToCreate(Task task);

    String validateQueueNameToUpdateTask(String name);

    String validateStatusToUpdateTask(Status status);

    Long countOfExecutableTasksByStatus(Long id, Status status);
}
