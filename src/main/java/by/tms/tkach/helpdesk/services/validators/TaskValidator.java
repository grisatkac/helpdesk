package by.tms.tkach.helpdesk.services.validators;

import by.tms.tkach.helpdesk.dto.form.DefaultDataTaskInputFields;
import by.tms.tkach.helpdesk.dto.task.TaskUpdateDTO;
import by.tms.tkach.helpdesk.dto.task.request.TaskCreateRequestDTO;
import by.tms.tkach.helpdesk.entities.Task;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.entities.enums.task.Status;
import by.tms.tkach.helpdesk.entities.enums.task.Urgency;
import by.tms.tkach.helpdesk.exception.EntityNotFound;
import by.tms.tkach.helpdesk.mappers.TaskMapper;
import by.tms.tkach.helpdesk.repositories.TaskRepository;
import by.tms.tkach.helpdesk.services.TaskQueueService;
import by.tms.tkach.helpdesk.services.TaskService;
import by.tms.tkach.helpdesk.services.UserService;
import by.tms.tkach.helpdesk.utils.AuthUserUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class TaskValidator {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskQueueService taskQueueService;
    private final UserService userService;
    private final TaskRepository taskRepository;

    public DefaultDataTaskInputFields prepareDefaultDataForTaskFields() {
        List<String> allQueueNames = taskQueueService.findAllNames();
        return DefaultDataTaskInputFields.builder()
                .queueNamesList(allQueueNames)
                .urgencyList(Arrays.asList(Urgency.values()))
                .build();
    }

    @Transactional
    public Task validateAndPrepareTaskForUpdate(TaskUpdateDTO taskUpdateDTO, BindingResult bindingResult) {
        Task taskToUpdate = null;

        TaskQueue queue = taskQueueService.getByName(taskUpdateDTO.getQueueName());

        if (queue == null) {
            bindingResult.rejectValue("queueName", "", "Incorrect task queue");
            return taskToUpdate;
        }

        taskToUpdate = taskRepository.findTaskDetails(taskUpdateDTO.getId());
        if (!Objects.equals(queue.getId(), taskToUpdate.getTaskQueue().getId())) {
            taskToUpdate.setTaskQueue(queue);
            taskToUpdate.setStatus(Status.PROCESSING);
        }

        taskMapper.updateTask(taskUpdateDTO, taskToUpdate);

        return taskToUpdate;
    }

    @Transactional
    public Task validateAndPrepareTaskFotCreate(TaskCreateRequestDTO taskCreateRequestDTO, BindingResult bindingResult) {
        TaskQueue foundQueue = taskQueueService.getByName(taskCreateRequestDTO.getTaskQueueName());
        Task taskToCreate = null;
        if (foundQueue == null) {
            bindingResult.rejectValue("queue", "", "This queue doesn't exist already. Please reload page to see actual queues");
        } else {
            taskToCreate = taskMapper.toTask(taskCreateRequestDTO);
            taskToCreate.setTaskQueue(foundQueue);

            User ownerUser = userService.findById(AuthUserUtils.getAuthUser().getId());
            taskToCreate.setOwnerUser(ownerUser);
            taskToCreate.setStatus(Status.PROCESSING);
        }

        return taskToCreate;
    }

    @Transactional
    public boolean prepareAndInteractWithTask(String operation, Long id) {

        Task taskForOperation = taskService.findById(id);
        Long userOwner = AuthUserUtils.getAuthUser().getId();
        boolean result = false;

        if (!Objects.equals(taskForOperation.getOwnerUser().getId(), userOwner)) {
            throw new EntityNotFound("not found");
        }


        if (operation.equals("take") && taskForOperation.getStatus().equals(Status.PROCESSING)) {
            User userExecutor = userService.findById(userOwner);
            return taskService.takeTask(id, userExecutor);
        }

        if (operation.equals("complete") && taskForOperation.getStatus().equals(Status.IN_PROGRESS)){
            return taskService.completeTask(id);
        }

        return result;
    }
}
