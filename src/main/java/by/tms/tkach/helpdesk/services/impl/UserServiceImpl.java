package by.tms.tkach.helpdesk.services.impl;

import by.tms.tkach.helpdesk.dto.auth.AuthUser;
import by.tms.tkach.helpdesk.dto.user.UserUpdateDTO;
import by.tms.tkach.helpdesk.dto.user.response.UserPagingResponseDTO;
import by.tms.tkach.helpdesk.entities.*;
import by.tms.tkach.helpdesk.entities.enums.task.Roles;
import by.tms.tkach.helpdesk.entities.enums.task.Status;
import by.tms.tkach.helpdesk.exception.CreateError;
import by.tms.tkach.helpdesk.exception.NotFoundException;
import by.tms.tkach.helpdesk.exception.UpdateError;
import by.tms.tkach.helpdesk.mappers.UserMapper;
import by.tms.tkach.helpdesk.repositories.DepartmentRepository;
import by.tms.tkach.helpdesk.repositories.TaskQueueRepository;
import by.tms.tkach.helpdesk.repositories.UserRepository;
import by.tms.tkach.helpdesk.services.TaskService;
import by.tms.tkach.helpdesk.services.UserService;
import by.tms.tkach.helpdesk.utils.AuthUserUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TaskService taskService;
    private final UserMapper userMapper;
    private final DepartmentRepository departmentRepository;
    private final TaskQueueRepository taskQueueRepository;
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User create(User user) {
        String errorMessage = validateAndPrepareErrorMessageUserForRegistration(user);

        if (!errorMessage.isEmpty()) {
            throw new CreateError(errorMessage);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Roles.ROLE_USER);

        return userRepository.save(user);
    }

    @Override
    public String validateAndPrepareErrorMessageUserForRegistration(User user) {
        String errorMessage = "";
        if (existUserLogin(user.getLogin())) {
            return "Login must be unique";
        }

        if (existUserEmail(user.getEmail())) {
            return "Email must be unique";
        }
        return errorMessage;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find user")));
    }

    @Override
    public User getByLogin(String login) {
        return userRepository
                .getByLogin(login).orElseThrow(() -> new EntityNotFoundException(String.format("User with login %s not exist", login)));
    }

    @Override
    public User findByLoginAndPassword(String login, String password) {
        return userRepository.getByLoginAndPassword(login, password)
                .orElseThrow(() -> new NotFoundException(String.format("User with login %s and password %s not exist", login, password)));
    }

    @Override
    public UserUpdateDTO getForUpdate(Long id) {
        User user = findById(id).orElseThrow(() -> new NotFoundException("Not found user"));
        return userMapper.toUserUpdate(user);
    }

    @Override
    public User getUserDetails(Long id) {
        return userRepository.getUserDetails(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with %s id not exist", id)));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserPagingResponseDTO getAllPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Page<User> page = getPage(pageNumber, pageSize, sortBy, sortDir);
        return UserPagingResponseDTO.builder()
                .currentPage(pageNumber)
                .totalPages(page.getTotalPages())
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .reverseSortDir(sortDir.equals("asc")? "desc" : "asc")
                .items(userMapper.toUserDetailsList(page.getContent()))
                .build();
    }

    private Page<User> getPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);
        return userRepository.findAll(paging);
    }

    @Transactional
    @Override
    public User update(User user) {
        User foundUserById = findById(user.getId()).orElseThrow(() -> new NotFoundException("Invalid id"));

        String errorMessage = validateUserToUpdate(user);

        if (!errorMessage.isEmpty()) {
            throw new UpdateError(errorMessage);
        }

        Optional<Department> foundDepartment = departmentRepository.findByName(user.getDepartment().getName());

        if (foundUserById.getDepartment() != null
                &&!foundUserById.getDepartment().equals(foundDepartment.get())
                && foundUserById.getDepartment().getHead() != null
                && foundUserById.getDepartment().getHead().getLogin().equals(foundUserById.getLogin()))
        {
            foundUserById.getDepartment().setHead(null);
            user.setDepartment(foundUserById.getDepartment());
            user.getDepartment().setHead(null);
            user.setQueues(null);
            user.setPassword(foundUserById.getPassword());
            userRepository.save(user);
            user.setDepartment(foundDepartment.get());
        } else{
            user.setDepartment(foundDepartment.get());
            user.setPassword(foundUserById.getPassword());
        }

        userRepository.save(user);
        return user;
    }

    @Transactional
    public String validateUserToUpdate(User user) {
        String errorMessage = "";
        Optional<User> userById = findById(user.getId());

        if (userById.isEmpty()) {
            return "Invalid id";
        }

        if (existUserEmail(user.getEmail()) && !userById.get().getEmail().equals(user.getEmail())) {
            return "Email should be unique";
        }

        if (existUserLogin(user.getLogin()) && !userById.get().getLogin().equals(user.getLogin())) {
            return "login should be unique";
        }

        if (Roles.valueOf(user.getRole().name()) == null) {
            return "Incorrect role";
        }

        if (user.getDepartment().getName().isEmpty()) {
            return "Department name can't be empty";
        }

        Optional<Department> departmentByName = departmentRepository.findByName(user.getDepartment().getName());

        if (!user.getDepartment().getName().isEmpty() && departmentByName.isEmpty()) {
            return "Department name is incorrect";
        }

        if (!userById.get().getDepartment().getName().equals(user.getDepartment().getName())) {
            Long countOfNotCompletedTasks = taskService.countOfExecutableTasksByStatus(user.getId(), Status.IN_PROGRESS);

            if (countOfNotCompletedTasks > 0L) {
                return "User must complete tasks in progress that can be move to another department";
            }
        }

        return errorMessage;
    }



    @Override
    public User delete(Long id) {
        throw new UpdateError("Deleting user are unavailable");
    }

    @Transactional
    @Override
    public boolean addTaskQueue(String queueName, Long userId) {
        if (queueName == null) {
            throw new NotFoundException("Invalid queue name");
        }

        if (userId == null) {
            throw new NotFoundException("Invalid user id");
        }

        User user = findById(userId).orElseThrow(() -> new NotFoundException("Invalid user id"));

        if (user.getDepartment() == null) {
            throw new UpdateError("User should be in department");
        }

        TaskQueue taskQueue = taskQueueRepository.findByName(queueName).orElseThrow(() -> new NotFoundException("Invalid queue name"));

        if (!Objects.equals(user.getDepartment(), taskQueue.getDepartment())) {
            throw new UpdateError("User and queue must be in one department. Choose the right queue for user");
        }

        List<TaskQueue> queues = user.getQueues();

        if (!queues.isEmpty() && queues.contains(taskQueue)) {
            throw new UpdateError("User already have this queue");
        } else {
            queues.add(taskQueue);
            user.setQueues(queues);
            userRepository.save(user);
        }

        return true;
    }

    @Override
    public boolean removeTaskQueue(String queueName, Long userId) {
        if (queueName == null) {
            throw new NotFoundException("Invalid queue name");
        }

        if (userId == null) {
            throw new NotFoundException("Invalid user id");
        }

        User user = findById(userId).orElseThrow(() -> new NotFoundException("Invalid user id"));
        TaskQueue taskQueue = taskQueueRepository.findByName(queueName).orElseThrow(() -> new NotFoundException("Invalid queue name"));

        if (!Objects.equals(user.getDepartment(), taskQueue.getDepartment())) {
            throw new UpdateError("User and queue must be in one department. Choose the right queue for user");
        }
        List<TaskQueue> queues = user.getQueues();

        if (!queues.isEmpty() && queues.contains(taskQueue)) {
            List<TaskQueue> newList = queues.stream()
                    .filter(queue -> !queue.getName().equals(taskQueue.getName()))
                    .collect(Collectors.toList());

            user.setQueues(newList);

            userRepository.save(user);

        } else {
            throw new UpdateError("User don't have this queue");
        }
        return true;
    }

    @Override
    public boolean existUserEmail(String email) {
        return userRepository.getByEmail(email).isPresent();
    }

    @Override
    public boolean existUserLogin(String login) {
        return userRepository.getByLogin(login).isPresent();
    }

    @Transactional
    @Override
    public boolean interactWithTask(String operation, Long taskId) {
        boolean result = false;

        Task taskForOperation =  taskService.getTaskWithQueue(taskId).orElseThrow(() -> new NotFoundException("Can't find task"));

        User interactingUser = findById(AuthUserUtils.getAuthUser().getId())
                .orElseThrow(() -> new NotFoundException("Can't find user"));

        if (taskForOperation.getTaskQueue() == null) {
            throw new NotFoundException("Error: task must be belong to some task queue");
        }

        Department departmentOfTheTask = taskForOperation.getTaskQueue().getDepartment();
        Department departmentOfInteractingUser = interactingUser.getDepartment();


        if (!departmentOfInteractingUser.getId().equals(departmentOfTheTask.getId())) {
            throw new UpdateError("User can't interact with this task: task must be in queue from user department");
        }

        if (!interactingUser.getQueues().contains(taskForOperation.getTaskQueue())) {
            throw new UpdateError("User don't have this task queue");
        }

        if (operation.equals("take") && taskForOperation.getStatus().equals(Status.PROCESSING)) {
            taskForOperation.setStatus(Status.IN_PROGRESS);
            taskForOperation.setExecutorUser(interactingUser);
            userRepository.save(interactingUser);
            return true;
        }


        if (operation.equals("complete") && taskForOperation.getStatus().equals(Status.IN_PROGRESS)) {
            taskForOperation.setStatus(Status.COMPLETED);
            userRepository.save(interactingUser);
            return true;
        }

        return result;
    }
}
