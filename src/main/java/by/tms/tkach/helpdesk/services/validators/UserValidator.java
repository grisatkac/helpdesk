package by.tms.tkach.helpdesk.services.validators;

import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.entities.Task;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.exception.UpdateError;
import by.tms.tkach.helpdesk.repositories.DepartmentRepository;
import by.tms.tkach.helpdesk.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserValidator {

    private final UserService userService;
    private final DepartmentRepository departmentRepository;

    public void validateToCreate(User user, BindingResult bindingResult) {
        if (userService.existUserLogin(user.getLogin())) {
            bindingResult.rejectValue("login", "", "Login must be unique");
        }

        if (userService.existUserEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "", "Email must be unique");
        }
    }

    @Transactional
    public void validateToUpdate(User user, BindingResult bindingResult) {
        Optional<User> userById = userService.findById(user.getId());

        if (userById.isEmpty()) {
            throw new UpdateError("Can't empty user");
        }

        if (userService.existUserEmail(user.getEmail()) && !userById.get().getEmail().equals(user.getEmail())) {
            bindingResult.rejectValue("email", "", "Email should be unique");
        }

        if (userService.existUserLogin(user.getLogin()) && !userById.get().getLogin().equals(user.getLogin())) {
            bindingResult.rejectValue("email", "", "login should be unique");
        }

        Optional<Department> departmentByName = departmentRepository.findByName(user.getDepartment().getName());

        if (!user.getDepartment().getName().isEmpty() && departmentByName.isEmpty()) {
            bindingResult.rejectValue("departmentName", "", "Department with this not exist");
        }

        if (!userById.get().getDepartment().getName().equals(user.getDepartment().getName())) {
            bindingResult.rejectValue("departmentName", "", "error to future");
        }
    }
}
