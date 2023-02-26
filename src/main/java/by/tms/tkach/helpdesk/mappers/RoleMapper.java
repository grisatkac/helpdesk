package by.tms.tkach.helpdesk.mappers;

import by.tms.tkach.helpdesk.dto.role.request.RoleRequestDTO;
import by.tms.tkach.helpdesk.dto.role.response.RoleResponseDTO;
import by.tms.tkach.helpdesk.entities.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponseDTO toRoleResponse(Role role);
    RoleRequestDTO toRoleRequest(Role role);
}
