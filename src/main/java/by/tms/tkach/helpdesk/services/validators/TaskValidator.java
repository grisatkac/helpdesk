package by.tms.tkach.helpdesk.services.validators;

import by.tms.tkach.helpdesk.dto.form.DefaultDataTaskInputFields;
import by.tms.tkach.helpdesk.entities.Task;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.entities.enums.task.Urgency;
import by.tms.tkach.helpdesk.exception.UpdateError;
import by.tms.tkach.helpdesk.services.TaskQueueService;
import by.tms.tkach.helpdesk.services.TaskService;
import by.tms.tkach.helpdesk.utils.AuthUserUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TaskValidator {
    private final TaskService taskService;
    private final TaskQueueService taskQueueService;
    public static boolean isValidToCreate = false;

    public DefaultDataTaskInputFields prepareDefaultDataForTaskFields() {
        List<String> allQueueNames = taskQueueService.findAllNames();
        return DefaultDataTaskInputFields.builder()
                .queueNamesList(allQueueNames)
                .urgencyList(Arrays.asList(Urgency.values()))
                .build();
    }

    public void validateToCreate(Task task, BindingResult bindingResult) {
        String errorMessage = taskService.validateTaskToCreate(task);

        if (!errorMessage.isEmpty()) {
            bindingResult.rejectValue("taskQueueName", "", errorMessage);
            isValidToCreate = false;
        } else {
            isValidToCreate = true;
        }
    }

    public void validateToUpdate(Task task, BindingResult bindingResult) {
        Task taskById = taskService.findById(task.getId()).orElseThrow(() -> new UpdateError("Incorrect id"));
        taskById.getStatus();
        String errorMessageOfValidationStatus = taskService.validateStatusToUpdateTask(taskById.getStatus());

        if (!errorMessageOfValidationStatus.isEmpty()) {
            bindingResult.rejectValue("status", "", errorMessageOfValidationStatus);
        }

        String errorMessageOfValidationQueueName = taskService.validateQueueNameToUpdateTask(task.getTaskQueue().getName());

        if (!errorMessageOfValidationQueueName.isEmpty()) {
            bindingResult.rejectValue("queueName", "", errorMessageOfValidationQueueName);
        }

    }

    @Transactional
    public boolean haveAccessToGetTask(Long taskId) {
        Optional<Task> task = taskService.findById(taskId);

        TaskQueue taskQueue = task.get().getTaskQueue();

        List<TaskQueue> userQueues = AuthUserUtils.getAuthUser().getQueues();

        if (task.isPresent()) {

        }

        return true;
    }
}
