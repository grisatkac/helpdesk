package by.tms.tkach.helpdesk.mappers;

import by.tms.tkach.helpdesk.dto.auth.AuthUser;
import by.tms.tkach.helpdesk.dto.user.request.UserCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.user.response.UserDetailsResponse;
import by.tms.tkach.helpdesk.dto.user.response.UserShortResponseDTO;
import by.tms.tkach.helpdesk.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateRequestDTO user);

    UserDetailsResponse toUserDetails(User user);

    UserShortResponseDTO toUserShort(User user);

    AuthUser toAuth(User user);
}
