package by.tms.tkach.helpdesk.services.processing;

import by.tms.tkach.helpdesk.dto.department.DepartmentUpdateDTO;
import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.mappers.DepartmentMapper;
import by.tms.tkach.helpdesk.services.DepartmentService;
import by.tms.tkach.helpdesk.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DepartmentProcessing {

    private final DepartmentService departmentService;
    private DepartmentMapper departmentMapper;
    private final UserService userService;

    public Department prepareToUpdate(DepartmentUpdateDTO department) {
        Department departmentToUpdate = departmentMapper.toDepartment(department);;

        User user = userService.getByLogin(department.getHeadLogin());

        departmentToUpdate.setHead(user);

        return departmentToUpdate;
    }
}
