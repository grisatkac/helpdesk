package by.tms.tkach.helpdesk.services.validators;

import by.tms.tkach.helpdesk.dto.taskQueue.TaskQueueUpdateDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.request.TaskQueueCreateRequestDTO;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.mappers.TaskQueueMapper;
import by.tms.tkach.helpdesk.services.impl.TaskQueueServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Component
@AllArgsConstructor
public class TaskQueueValidator {

    private final TaskQueueServiceImpl taskQueueService;
    private final TaskQueueMapper taskQueueMapper;

    public TaskQueue validateAndPrepareForCreate(TaskQueueCreateRequestDTO taskQueue, BindingResult bindingResult) {
        TaskQueue taskQueueToCreate = null;

        TaskQueue taskQueueByName = taskQueueService.getByName(taskQueue.getName());

        if (taskQueueByName != null) {
            bindingResult.rejectValue("name", "", "Name of task queue must be unique");
        } else{
            taskQueueToCreate = taskQueueMapper.toTaskQueue(taskQueue);
        }

        return taskQueueToCreate;
    }

    public TaskQueue validateAndPrepareForUpdate(TaskQueueUpdateDTO taskQueue, BindingResult bindingResult) {
        TaskQueue taskQueueToUpdate = null;
        TaskQueue byName = taskQueueService.getByName(taskQueue.getName());

        if (byName == null) {
            return taskQueueMapper.toTaskQueue(taskQueue);
        }

        if (!Objects.equals(byName.getId(), taskQueue.getId())) {
            bindingResult.reject("name", "You can't update task queue name because task queue name must be unique and already exist");
        } else {
            taskQueueToUpdate = taskQueueMapper.toTaskQueue(taskQueue);
        }

        return taskQueueToUpdate;
    }
}
