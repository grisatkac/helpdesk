package by.tms.tkach.helpdesk.services.impl;

import by.tms.tkach.helpdesk.dto.task.TaskUpdateDTO;
import by.tms.tkach.helpdesk.dto.task.response.TaskDetailsResponseDTO;
import by.tms.tkach.helpdesk.dto.task.TaskPagingResponseDTO;
import by.tms.tkach.helpdesk.entities.Task;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.entities.enums.task.Status;
import by.tms.tkach.helpdesk.exception.CreateError;
import by.tms.tkach.helpdesk.exception.NotFoundException;
import by.tms.tkach.helpdesk.exception.UpdateError;
import by.tms.tkach.helpdesk.mappers.TaskMapper;
import by.tms.tkach.helpdesk.repositories.TaskQueueRepository;
import by.tms.tkach.helpdesk.repositories.TaskRepository;
import by.tms.tkach.helpdesk.repositories.UserRepository;
import by.tms.tkach.helpdesk.services.TaskQueueService;
import by.tms.tkach.helpdesk.services.TaskService;
import by.tms.tkach.helpdesk.services.validators.TaskValidator;
import by.tms.tkach.helpdesk.utils.AuthUserUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskQueueService taskQueueService;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Task create(Task task) {
        if (!AuthUserUtils.isAuthUserHasDepartment()) {
            throw new CreateError("Please contact with administrator: user without department can't create tasks");
        }

        if (task == null) {
            throw new CreateError("Task can't be empty");
        }

        if (!TaskValidator.isValidToCreate) {
            String errorMessageOfValidationTaskQueue = validateTaskToCreate(task);

            if (!errorMessageOfValidationTaskQueue.isEmpty()) {
                throw new CreateError(errorMessageOfValidationTaskQueue);
            }
        }

        Optional<TaskQueue> foundTaskQueue = taskQueueService.getByName(task.getTaskQueue().getName());

        task.setCreatedDate(LocalDate.now());
        task.setOwnerUser(userRepository.findById(AuthUserUtils.getAuthUser().getId()).get());
        task.setTaskQueue(foundTaskQueue.get());
        task.setStatus(Status.PROCESSING);

        return taskRepository.save(task);
    }

    @Override
    public String validateTaskToCreate(Task task) {
        String errorMessage = "";

        Optional<TaskQueue> foundQueue = taskQueueService.getByName(task.getTaskQueue().getName());

        if (foundQueue.isEmpty()) {
            return "Invalid task queue name";
        }

        if (foundQueue.get().getDepartment() == null) {
            return "Please contact with administrator: queue without department can't receive tasks";
        }

        return errorMessage;
    }


    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found")));
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Transactional
    @Override
    public Task update(Task task) {

        if (task == null) {
            throw new UpdateError("Task can't be empty");
        }

        Task taskById = findById(task.getId()).orElseThrow(() ->new UpdateError("Incorrect id"));

        String errorMessageOfValidationStatus = validateStatusToUpdateTask(taskById.getStatus());

        if (!errorMessageOfValidationStatus.isEmpty()) {
            throw  new UpdateError(errorMessageOfValidationStatus);
        }

        String errorMessageOfValidationQueueName = validateQueueNameToUpdateTask(task.getTaskQueue().getName());

        if (!errorMessageOfValidationQueueName.isEmpty()) {
            throw new UpdateError(errorMessageOfValidationQueueName);
        }

        Optional<TaskQueue> queueByName = taskQueueService.getByName(task.getTaskQueue().getName());

        taskById.setName(task.getName());
        taskById.setDescription(task.getDescription());
        taskById.setUrgency(task.getUrgency());
        taskById.setTaskQueue(queueByName.get());

        return taskRepository.save(taskById);
    }

    @Override
    public String validateQueueNameToUpdateTask(String name) {
        Optional<TaskQueue> queueByName = taskQueueService.getByName(name);

        if (queueByName.isEmpty()) {
            return "Incorrect name of queue";
        }

        return "";
    }

    @Override
    public String validateStatusToUpdateTask(Status status) {
        return (!status.name().equals(Status.PROCESSING.name())) ? "You can't update because your task in progress" : "";
    }

    @Override
    public Long countOfExecutableTasksByStatus(Long id, Status status) {
        Long countOfExecutableTasksByStatus = taskRepository.findCountOfExecutableTasksByStatus(7L, Status.IN_PROGRESS);
        return countOfExecutableTasksByStatus;
    }

    @Transactional
    @Override
    public Task delete(Long id) {
        Task task = findById(id).orElseThrow(() -> new UpdateError("Can't delete task with incorrect id"));

        if (!task.getStatus().name().equals(Status.PROCESSING.name())) {
            throw new UpdateError("You can't delete because your task in progress");
        }

        task.setOwnerUser(null);
        task.setTaskQueue(null);

        taskRepository.save(task);
        taskRepository.deleteById(id);
        return task;
    }

    @Transactional
    @Override
    public TaskDetailsResponseDTO findTaskDetails(Long id) {
        Task taskDetails = taskRepository.findTaskDetails(id);

        if (taskDetails == null) {
            throw new NotFoundException("Task not exist");
        }

        return taskMapper.toTaskDetails(taskDetails);
    }

    @Override
    public TaskUpdateDTO getForUpdate(Long id) {
        Task foundTask = taskRepository.findTaskWithQueue(id).orElseThrow(() -> new NotFoundException("Can't find task"));

        return taskMapper.toTaskUpdate(foundTask);
    }

    @Override
    public TaskPagingResponseDTO getAllPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String requestURI) {
        Page<Task> page = getPage(pageNumber, pageSize, sortBy, sortDir, null);
        return TaskPagingResponseDTO.builder()
                .currentPage(pageNumber)
                .totalPages(page.getTotalPages())
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .reverseSortDir(sortDir.equals("asc") ? "desc" : "asc")
                .authUserId(AuthUserUtils.getAuthUser().getId())
                .items(taskMapper.toTaskDetailsList(page.getContent()))
                .currentURI(requestURI)
                .build();
    }

    @Transactional
    @Override
    public TaskPagingResponseDTO getAllPageableByFilter(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String filter, String requestURI) {
        Page<Task> page = getPage(pageNumber, pageSize, sortBy, sortDir, filter);
        return TaskPagingResponseDTO.builder()
                .currentPage(pageNumber)
                .totalPages(page.getTotalPages())
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .reverseSortDir(sortDir.equals("asc") ? "desc" : "asc")
                .items(taskMapper.toTaskDetailsList(page.getContent()))
                .authUserId(AuthUserUtils.getAuthUser().getId())
                .currentURI(requestURI)
                .build();
    }

    @Override
    public Optional<Task> getTaskWithQueue(Long id) {
        return Optional.ofNullable(taskRepository.findTaskWithQueue(id).orElseThrow(() -> new NotFoundException("Task not found")));
    }

    @Transactional
    @Override
    public Page<Task> getPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String filter) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);
        if (filter == null) {
            return taskRepository.findAll(paging);
        }


        Long userId = AuthUserUtils.getAuthUser().getId();
        if (filter.equals("assignable")) {
            return taskRepository.findAllByOwnerUserId(userId, paging);
        }

        if (filter.equals("executable")) {
            return taskRepository.findAllByExecutorUserId(userId, paging);
        }

        if (filter.equals("toProcess")) {
            Optional<User> user = userRepository.findById(AuthUserUtils.getAuthUser().getId());
            List<TaskQueue> queues = user.get().getQueues();

            int t = 0;
            return taskRepository
                    .findByStatusAndAndExecutorUserAndTaskQueueIn(
                            Status.PROCESSING,
                            null,
                            queues,
                            paging
                    );
        }

        return null;
    }
}
