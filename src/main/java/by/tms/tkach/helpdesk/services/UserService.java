package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.dto.auth.AuthUser;
import by.tms.tkach.helpdesk.dto.user.request.UserCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.user.response.UserDetailsResponse;
import by.tms.tkach.helpdesk.dto.user.response.UserShortResponseDTO;
import by.tms.tkach.helpdesk.entities.User;
import org.springframework.validation.BindingResult;

public interface UserService extends CrudService<User> {

    AuthUser getUserForAuthContext(String login);

    User getByLogin(String login);

    User findByLoginAndPassword(String login, String password);

    UserDetailsResponse getUserDetails(Long id);

    UserShortResponseDTO getShortUser(Long id);

    User registerUser(UserCreateRequestDTO user);

    boolean existUserEmail(String email);

    boolean existUserLogin(String email);

    void validateUserForRegistration(UserCreateRequestDTO user, BindingResult bindingResult);

    void validateUserForHeadOfDepartment(String login, BindingResult bindingResult);
}
