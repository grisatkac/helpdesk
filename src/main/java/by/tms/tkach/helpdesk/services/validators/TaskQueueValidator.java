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
import java.util.Optional;

@Component
@AllArgsConstructor
public class TaskQueueValidator {

    private final TaskQueueServiceImpl taskQueueService;
    private final TaskQueueMapper taskQueueMapper;
    public static boolean isValidToCreate = false;
    public static boolean isValidToUpdate = false;

    public void validateQueueToCreate(TaskQueue taskQueue, BindingResult bindingResult) {
        String errorMessage = taskQueueService.validateTaskQueueNameToCreate(taskQueue);

        if (!errorMessage.isEmpty()) {
            bindingResult.rejectValue("name", "", errorMessage);
            isValidToCreate = false;
        } else {
            isValidToCreate = true;
        }
    }

    public void validateQueueToUpdate(TaskQueue taskQueue, BindingResult bindingResult) {
        String errorMessageOfValidationTaskQueueName = taskQueueService.validateNameToUpdate(taskQueue);

        if (!errorMessageOfValidationTaskQueueName.isEmpty()) {
            bindingResult.rejectValue("name", "", errorMessageOfValidationTaskQueueName);
            isValidToUpdate = false;
        }

        String errorMessageOfValidationDepartmentName = taskQueueService.validateDepartmentNameToUpdate(taskQueue);

        if (!errorMessageOfValidationDepartmentName.isEmpty()) {
            bindingResult.rejectValue("departmentName", "", errorMessageOfValidationDepartmentName);
            isValidToUpdate = false;
        }


        if (errorMessageOfValidationTaskQueueName.isEmpty() && errorMessageOfValidationDepartmentName.isEmpty()) {
            isValidToUpdate = true;
        }
    }
}
