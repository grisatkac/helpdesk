package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.dto.department.DepartmentUpdateDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentDetailsResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentPagingResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentShortResponseDTO;
import by.tms.tkach.helpdesk.entities.Department;

import java.util.Optional;

public interface DepartmentService extends CrudService<Department> {

    Department getByHeadId(Long userId);

    Optional<Department> getByName(String name);

    DepartmentUpdateDTO getForUpdate(Long id);

    DepartmentDetailsResponseDTO getDepartmentDetails(Long id);

    DepartmentShortResponseDTO getShortDepartment(Long id);

    DepartmentPagingResponseDTO getAllPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    Department getByHeadLogin(String login);

    String validateDepartmentNameToCreate(String name);

    String validateNameToUpdate(Department department);

    String validateHeadLoginToUpdate(Department department);

}
