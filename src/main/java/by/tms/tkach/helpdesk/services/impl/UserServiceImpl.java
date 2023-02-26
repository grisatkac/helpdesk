package by.tms.tkach.helpdesk.services.impl;

import by.tms.tkach.helpdesk.dto.auth.AuthUser;
import by.tms.tkach.helpdesk.dto.user.request.UserCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.user.response.UserDetailsResponse;
import by.tms.tkach.helpdesk.dto.user.response.UserShortResponseDTO;
import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.entities.Role;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.entities.enums.task.Roles;
import by.tms.tkach.helpdesk.exception.EntityNotFound;
import by.tms.tkach.helpdesk.mappers.DepartmentMapper;
import by.tms.tkach.helpdesk.mappers.UserMapper;
import by.tms.tkach.helpdesk.repositories.UserRepository;
import by.tms.tkach.helpdesk.services.DepartmentService;
import by.tms.tkach.helpdesk.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DepartmentMapper departmentMapper;
    private final RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        Role role = roleService.findByName(Roles.ROLE_USER.name());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFound("Can't find user"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public User delete(Long id) {
        return null;
    }

    @Transactional
    @Override
    public AuthUser getUserForAuthContext(String login) {
        User user = getByLogin(login);
        if (user == null) {
            throw new EntityNotFound(String.format("Can't find user with login %s", login));
        }

        user.setDepartment(user.getDepartment());

        return userMapper.toAuth(user);
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.getByLogin(login);
    }

    @Override
    public User findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    @Override
    public UserDetailsResponse getUserDetails(Long id) {
        User foundUser = userRepository.getUserDetails(id);
        return userMapper.toUserDetails(foundUser);
    }

    @Override
    public UserShortResponseDTO getShortUser(Long id) {
        return userMapper.toUserShort(userRepository.getShortUser(id));
    }

    @Override
    public User registerUser(UserCreateRequestDTO user) {
        return create(userMapper.toUser(user));
    }

    @Override
    public boolean existUserEmail(String email) {
        return userRepository.getByEmail(email) != null;
    }

    @Override
    public boolean existUserLogin(String login) {
        return userRepository.getByLogin(login) != null;
    }

    @Override
    public void validateUserForRegistration(UserCreateRequestDTO user, BindingResult bindingResult) {
        if (existUserEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "", "Current email already exists");
        }

        if (existUserLogin(user.getLogin())) {
            bindingResult.rejectValue("login", "", "Current login already exist");
        }
    }

    @Override
    public void validateUserForHeadOfDepartment(String login, BindingResult bindingResult) {
        if (userRepository.getByLogin(login).getDepartment() != null) {
            bindingResult.rejectValue("headLogin", "", "This user already a head of department");
        }

    }
}
