package by.tms.tkach.helpdesk.mappers;

import by.tms.tkach.helpdesk.dto.department.DepartmentUpdateDTO;
import by.tms.tkach.helpdesk.dto.department.request.DepartmentCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentDetailsResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentPagingResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentShortResponseDTO;
import by.tms.tkach.helpdesk.entities.Department;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DepartmentMapper {

    Department toDepartment(DepartmentUpdateDTO department);

    Department toDepartment(DepartmentCreateRequestDTO department);

    DepartmentResponseDTO toDepartmentResponse(Department department);

    DepartmentDetailsResponseDTO toDepartmentDetails(Department department);

    List<DepartmentDetailsResponseDTO> toDepartmentDetailsList(List<Department> departments);

    DepartmentShortResponseDTO toShortDepartment(Department department);

    DepartmentUpdateDTO toDepartmentUpdate(Department department);



    List<DepartmentPagingResponseDTO> toPagingResponse(List<Department> departments);
}
