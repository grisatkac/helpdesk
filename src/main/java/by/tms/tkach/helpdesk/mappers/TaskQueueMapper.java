package by.tms.tkach.helpdesk.mappers;

import by.tms.tkach.helpdesk.dto.taskQueue.TaskQueueUpdateDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.request.TaskQueueCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueueDetailsResponse;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueueShortResponseDTO;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
public interface TaskQueueMapper {

    TaskQueueDetailsResponse toTaskDetails(TaskQueue taskQueue);

    List<TaskQueueDetailsResponse> toTaskQueueDetailsList(List<TaskQueue> taskQueues);

    TaskQueueUpdateDTO toTaskQueueUpdate(TaskQueue taskQueue);

    TaskQueueShortResponseDTO toShortTaskQueue(TaskQueue taskQueue);

    TaskQueue toTaskQueue(TaskQueueCreateRequestDTO taskQueue);

    @Mapping(source = "departmentName", target = "department.name")
    TaskQueue toTaskQueue(TaskQueueUpdateDTO taskQueue);
}
