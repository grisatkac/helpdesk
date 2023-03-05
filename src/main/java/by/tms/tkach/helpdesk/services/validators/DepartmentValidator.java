package by.tms.tkach.helpdesk.services.validators;

import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.services.DepartmentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@AllArgsConstructor
@Data
public class DepartmentValidator {

    private final DepartmentService departmentService;
    public static boolean isValidToCreate = false;
    public static boolean isIsValidToUpdate = false;

    public void validateDepartmentNameToCreate(String name, BindingResult bindingResult) {
        String errorMessage = departmentService.validateDepartmentNameToCreate(name);

        if (!errorMessage.isEmpty()) {
            bindingResult.rejectValue("name", "", errorMessage);
            isValidToCreate = false;
        } else {
            isValidToCreate = true;
        }
    }

    public void validateDepartmentToUpdate(Department department, BindingResult bindingResult) {

        String departmentNameValidationErrorMessage = departmentService.validateNameToUpdate(department);

        if (!departmentNameValidationErrorMessage.isEmpty()) {
            bindingResult.rejectValue("name", "", departmentNameValidationErrorMessage);
            isIsValidToUpdate = false;
        }

        String headLoginValidationErrorMessage = departmentService.validateHeadLoginToUpdate(department);

        if (!headLoginValidationErrorMessage.isEmpty()) {
            bindingResult.rejectValue("headLogin", "", headLoginValidationErrorMessage);
            isIsValidToUpdate = false;
        } else {
            isIsValidToUpdate = true;
        }
    }
}
