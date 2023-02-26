package by.tms.tkach.helpdesk.services.impl;

import by.tms.tkach.helpdesk.dto.auth.AuthUser;
import by.tms.tkach.helpdesk.dto.task.TaskUpdateDTO;
import by.tms.tkach.helpdesk.dto.task.response.TaskDetailsResponseDTO;
import by.tms.tkach.helpdesk.dto.task.response.TaskUpdateResponseDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueuePagingResponseDTO;
import by.tms.tkach.helpdesk.dto.user.TaskPagingResponseDTO;
import by.tms.tkach.helpdesk.entities.Task;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.entities.enums.task.Status;
import by.tms.tkach.helpdesk.exception.EntityNotFound;
import by.tms.tkach.helpdesk.mappers.TaskMapper;
import by.tms.tkach.helpdesk.repositories.TaskRepository;
import by.tms.tkach.helpdesk.services.TaskService;
import by.tms.tkach.helpdesk.utils.AuthUserUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.extras.springsecurity5.auth.AuthUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Task create(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFound("Task not found"));
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task update(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task delete(Long id) {
        return null;
    }

    @Transactional
    @Override
    public TaskDetailsResponseDTO findTaskDetails(Long id) {
        Task taskDetails = taskRepository.findTaskDetails(id);

        return taskMapper.toTaskDetails(taskDetails);
    }

    @Override
    public TaskUpdateResponseDTO getForUpdate(Long id) {
        Task foundTask = taskRepository.findTaskWithQueue(id);

        return TaskUpdateResponseDTO.builder().taskUpdate(taskMapper.toTaskUpdate(foundTask)).build() /*taskMapper.toTaskUpdateResponse(foundTask)*/;

    }

    @Override
    public TaskPagingResponseDTO getAllPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Page<Task> page = getPage(pageNumber, pageSize, sortBy, sortDir, null);
        return TaskPagingResponseDTO.builder()
                .currentPage(pageNumber)
                .totalPages(page.getTotalPages())
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .reverseSortDir(sortDir.equals("asc") ? "desc" : "asc")
                .items(taskMapper.toTaskDetailsList(page.getContent()))
                .build();
    }

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

    @Transactional
    @Override
    public boolean completeTask(Long id) {
        Task task = findById(id);
        task.setStatus(Status.COMPLETED);
        taskRepository.save(task);
        return true;
    }

    @Transactional
    @Override
    public boolean takeTask(Long id, User user) {
        Task task = findById(id);
        task.setExecutorUser(user);
        task.setStatus(Status.IN_PROGRESS);
        taskRepository.save(task);
        return true;
    }

    private Page<Task> getPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String filter) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);
        if (filter == null) {
            return taskRepository.findAll(paging);
        }

        Long userId = AuthUserUtils.getAuthUser().getId();
        if (filter.equals("assignable")) {
            return taskRepository.findAllAssignableTasks(userId, paging);
        }

        if (filter.equals("executable")) {
            return taskRepository.findAllExecutableTasks(userId, paging);
        }

        return null; // MAY BE THROW EXECPETION BAD REQUEST
    }
}
