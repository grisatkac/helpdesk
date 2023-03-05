package by.tms.tkach.helpdesk.mappers;

import by.tms.tkach.helpdesk.dto.department.DepartmentUpdateDTO;
import by.tms.tkach.helpdesk.dto.department.request.DepartmentCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentDetailsResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentPagingResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentShortResponseDTO;
import by.tms.tkach.helpdesk.entities.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DepartmentMapper {

    @Mapping(source = "headLogin", target = "head.login")
    Department toDepartment(DepartmentUpdateDTO department);

    Department toDepartment(DepartmentCreateRequestDTO department);

    DepartmentDetailsResponseDTO toDepartmentDetails(Department department);

    List<DepartmentDetailsResponseDTO> toDepartmentDetailsList(List<Department> departments);

    DepartmentShortResponseDTO toShortDepartment(Department department);

    @Mapping(source = "head.login", target = "headLogin")
    DepartmentUpdateDTO toDepartmentUpdate(Department department);

}
