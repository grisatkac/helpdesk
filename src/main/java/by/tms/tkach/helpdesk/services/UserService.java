package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.dto.user.UserUpdateDTO;
import by.tms.tkach.helpdesk.dto.user.response.UserPagingResponseDTO;
import by.tms.tkach.helpdesk.entities.User;

public interface UserService extends CrudService<User> {

    User getByLogin(String login);

    User findByLoginAndPassword(String login, String password);

    User getUserDetails(Long id);

    UserUpdateDTO getForUpdate(Long id);

    UserPagingResponseDTO getAllPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    String validateAndPrepareErrorMessageUserForRegistration(User user);

    boolean existUserEmail(String email);

    boolean existUserLogin(String email);

    boolean interactWithTask(String operation, Long taskId);

    boolean addTaskQueue(String queueName, Long userId);

    boolean removeTaskQueue(String queueName, Long userId);
}
