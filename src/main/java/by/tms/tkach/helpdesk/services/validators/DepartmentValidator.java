package by.tms.tkach.helpdesk.services.validators;

import by.tms.tkach.helpdesk.dto.department.DepartmentUpdateDTO;
import by.tms.tkach.helpdesk.dto.department.request.DepartmentCreateRequestDTO;
import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.mappers.DepartmentMapper;
import by.tms.tkach.helpdesk.services.DepartmentService;
import by.tms.tkach.helpdesk.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Component
@AllArgsConstructor
public class DepartmentValidator {

    private final DepartmentService departmentService;
    private DepartmentMapper departmentMapper;
    private final UserService userService;

    @Transactional
    public Department validateAndPrepareDepartmentForUpdate(DepartmentUpdateDTO department, BindingResult bindingResult) {
        Department departmentToUpdate = null;
        Department byName = departmentService.getByName(department.getName());

        if (byName != null && !Objects.equals(byName.getId(), department.getId())) {
            bindingResult.rejectValue(
                    "name", "",
                    "You can't update department name because department name must be unique and already exist");
        }

        User user = userService.getByLogin(department.getHeadLogin());
        Department userDepartment = null;

        if (user != null) {
            userDepartment = user.getDepartment();
        }

        if (userDepartment != null && !userDepartment.equals(byName)) {
            bindingResult.rejectValue("headLogin", "", "This user are head of another department");
        } else {
            departmentToUpdate = departmentMapper.toDepartment(department);
            departmentToUpdate.setHead(user);
        }

        return departmentToUpdate;
    }

    @Transactional
    public Department validateAndPrepareDepartmentForCreate(DepartmentCreateRequestDTO departmentCreateRequest, BindingResult bindingResult) {
        Department departmentToCreate = null;


        Department existDepartment = departmentService.getByName(departmentCreateRequest.getName());

        if (existDepartment != null) {
            bindingResult.rejectValue("name", "", "Department must be have unique name");
        } else {
            departmentToCreate = departmentMapper.toDepartment(departmentCreateRequest);
        }

        return departmentToCreate;
    }
}


