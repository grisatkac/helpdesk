package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.dto.taskQueue.TaskQueueUpdateDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueueDetailsResponse;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueuePagingResponseDTO;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.entities.User;

import java.util.List;
import java.util.Optional;

public interface TaskQueueService extends CrudService<TaskQueue> {

    TaskQueueDetailsResponse getTaskQueueDetails(Long id);

    Optional<TaskQueue> getByName(String name);

    TaskQueueUpdateDTO getForUpdate(Long id);

    TaskQueuePagingResponseDTO getAllPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    TaskQueuePagingResponseDTO getAllUserQueuesPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, User user);

    List<String> findAllNames();

    String validateTaskQueueNameToCreate(TaskQueue taskQueue);
}
