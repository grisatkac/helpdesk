package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.dto.taskQueue.TaskQueueUpdateDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueueDetailsResponse;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueuePagingResponseDTO;
import by.tms.tkach.helpdesk.entities.TaskQueue;

import java.util.List;

public interface TaskQueueService extends CrudService<TaskQueue> {

    TaskQueueDetailsResponse getTaskQueueDetails(Long id);

    TaskQueue getByName(String name);

    TaskQueueUpdateDTO getForUpdate(Long id);

    TaskQueuePagingResponseDTO getAllPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    List<String> findAllNames();

}
