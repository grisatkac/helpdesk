package by.tms.tkach.helpdesk.mappers;

import by.tms.tkach.helpdesk.dto.auth.AuthUser;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentResponseDTO;
import by.tms.tkach.helpdesk.dto.user.UserUpdateDTO;
import by.tms.tkach.helpdesk.dto.user.request.UserCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.user.response.UserDetailsResponse;
import by.tms.tkach.helpdesk.dto.user.response.UserShortResponseDTO;
import by.tms.tkach.helpdesk.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring"/*, uses = {RoleMapper.class}*/)
public interface UserMapper {

    User toUser(UserCreateRequestDTO user);

    User toUser(AuthUser user);

    //@Mapping(source = "role", target = "role")
    @Mapping(source = "departmentName", target = "department.name")
    User toUser(UserUpdateDTO user);

    UserDetailsResponse toUserDetails(User user);

    List<UserDetailsResponse> toUserDetailsList(List<User> users);

    UserShortResponseDTO toUserShort(User user);

    AuthUser toAuth(User user);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "role", target = "role")
    UserUpdateDTO toUserUpdate(User user);

    @Mapping(source = "departmentName", target = "department.name")
    void updateUser(UserUpdateDTO user, @MappingTarget User userToUpdate);
}
