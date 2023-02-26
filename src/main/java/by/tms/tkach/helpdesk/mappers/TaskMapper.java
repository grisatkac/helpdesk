package by.tms.tkach.helpdesk.mappers;

import by.tms.tkach.helpdesk.dto.task.TaskUpdateDTO;
import by.tms.tkach.helpdesk.dto.task.request.TaskCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.task.response.TaskDetailsResponseDTO;
import by.tms.tkach.helpdesk.dto.task.response.TaskResponseDTO;
import by.tms.tkach.helpdesk.dto.task.response.TaskUpdateResponseDTO;
import by.tms.tkach.helpdesk.entities.Task;
import lombok.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TaskQueueMapper.class})
public interface TaskMapper {

    @Mapping(source = "ownerUser", target = "userOwner")
    @Mapping(source = "executorUser", target = "userExecutor")
    @Mapping(source = "taskQueue", target = "queue")
    TaskDetailsResponseDTO toTaskDetails(Task task);

    List<TaskDetailsResponseDTO> toTaskDetailsList(List<Task> tasks);

    Task toTask(TaskCreateRequestDTO task);

    Task toTask(TaskUpdateDTO task);

    TaskResponseDTO toTaskResponse(Task task);


    @Mapping(source = "taskQueue.name", target = "queueName")
    TaskUpdateDTO toTaskUpdate(Task task);

    /*@Mapping(source = "taskQueue.name", target = "taskUpdate.queueName")
    @Mapping(source = "urgency", target = "taskUpdate.urgency")
    TaskUpdateResponseDTO toTaskUpdateResponse(Task task);*/

    void updateTask(TaskUpdateDTO taskUpdateDTO, @MappingTarget Task task);
}
