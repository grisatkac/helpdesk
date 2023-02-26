package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.dto.department.DepartmentUpdateDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentDetailsResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentPagingResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentShortResponseDTO;
import by.tms.tkach.helpdesk.entities.Department;

public interface DepartmentService extends CrudService<Department> {

    Department getByHeadId(Long id);

    Department getByName(String name);

    DepartmentUpdateDTO getForUpdate(Long id);

    DepartmentDetailsResponseDTO getDepartmentDetails(Long id);

    DepartmentShortResponseDTO getShortDepartment(Long id);

    DepartmentPagingResponseDTO getAllPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    DepartmentUpdateDTO updateDepartment(Long id, DepartmentUpdateDTO department);

    boolean isExist(String name);
}
