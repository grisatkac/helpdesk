package by.tms.tkach.helpdesk.services.impl;

import by.tms.tkach.helpdesk.dto.taskQueue.TaskQueueUpdateDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueueDetailsResponse;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueuePagingResponseDTO;
import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.exception.CreateError;
import by.tms.tkach.helpdesk.exception.NotFoundException;
import by.tms.tkach.helpdesk.exception.UpdateError;
import by.tms.tkach.helpdesk.mappers.TaskQueueMapper;
import by.tms.tkach.helpdesk.repositories.DepartmentRepository;
import by.tms.tkach.helpdesk.repositories.TaskQueueRepository;
import by.tms.tkach.helpdesk.services.TaskQueueService;
import by.tms.tkach.helpdesk.services.validators.TaskQueueValidator;
import by.tms.tkach.helpdesk.utils.AuthUserUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class TaskQueueServiceImpl implements TaskQueueService {

    private final TaskQueueRepository taskQueueRepository;
    private final TaskQueueMapper taskQueueMapper;
    private final DepartmentRepository departmentRepository;

    @Transactional
    @Override
    public TaskQueue create(TaskQueue taskQueue) {

        if (taskQueue == null) {
            throw new CreateError("Task queue is empty");
        }

        if (!TaskQueueValidator.isValidToCreate) {
            String errorMessage = validateTaskQueueNameToCreate(taskQueue);

            if (!errorMessage.isEmpty()) {
                throw new CreateError(errorMessage);
            }
        }

        return taskQueueRepository.save(taskQueue);
    }

    @Override
    public String validateTaskQueueNameToCreate(TaskQueue taskQueue) {
        String errorMessage = "";
        if (getByName(taskQueue.getName()).isPresent()) {
            errorMessage = "Name of the task must be unique";
        }

        return errorMessage;
    }



    @Override
    public Optional<TaskQueue> findById(Long id) {
        return taskQueueRepository.findById(id);
    }

    @Override
    public List<TaskQueue> getAll() {
        return null;
    }

    @Transactional
    @Override
    public TaskQueue update(TaskQueue taskQueue) {

        if (taskQueue == null) {
            throw  new UpdateError("Task queue can't be empty");
        }

        if (!TaskQueueValidator.isValidToUpdate) {
            String errorMessageOfValidationTaskQueueName = validateNameToUpdate(taskQueue);

            if (!errorMessageOfValidationTaskQueueName.isEmpty()) {
                throw new UpdateError(errorMessageOfValidationTaskQueueName);
            }

            String errorMessageOfValidationDepartmentName = validateDepartmentNameToUpdate(taskQueue);

            if (!errorMessageOfValidationDepartmentName.isEmpty()) {
                throw new UpdateError(errorMessageOfValidationDepartmentName);
            }
        }

        Optional<Department> departmentByName = departmentRepository.findByName(taskQueue.getDepartment().getName());

        taskQueue.setDepartment(departmentByName.get());


        return taskQueueRepository.save(taskQueue);
    }

    public String validateNameToUpdate(TaskQueue taskQueue) {
        String errorMessage = "";

        Optional<TaskQueue> queueByName = getByName(taskQueue.getName());

        if (queueByName.isPresent() && !Objects.equals(queueByName.get().getId(), taskQueue.getId())) {
            return "Task queue name must be unique and already exist";
        }

        return errorMessage;
    }

    public String validateDepartmentNameToUpdate(TaskQueue taskQueue) {
        String errorMessage = "";

        if (!taskQueue.getDepartment().getName().isEmpty()) {
            Optional<Department> departmentByName = departmentRepository.findByName(taskQueue.getDepartment().getName());

            if (departmentByName.isEmpty()) {
                return "Department name not exist";
            }

            Optional<TaskQueue> queueById = findById(taskQueue.getId());

            if (queueById.get().getDepartment() != null
                    && !queueById.get().getDepartment().getId().equals(departmentByName.get().getId())
                    && (queueById.get().getUsers().size() != 0 || queueById.get().getTasks().size() != 0)) {
                return "You can't move queue to another department";
            }
        }

        return errorMessage;
    }

    @Transactional
    @Override
    public TaskQueue delete(Long id) {
        TaskQueue taskQueue = findById(id).orElseThrow(() -> new NotFoundException("Can't delete queue with incorrect id"));

        if (taskQueue.getUsers().size() != 0 || taskQueue.getTasks().size() != 0) {
            throw new UpdateError("Queue is not empty");
        }

        taskQueue.setDepartment(null);

        taskQueueRepository.save(taskQueue);
        taskQueueRepository.deleteById(id);
        return taskQueue;
    }

    @Override
    public TaskQueueDetailsResponse getTaskQueueDetails(Long id) {
        TaskQueue foundTaskQueue = taskQueueRepository.findTaskQueueDetails(id);
        if (foundTaskQueue == null) {
            throw new NotFoundException("Can't find task queue");
        }
        return taskQueueMapper.toTaskDetails(foundTaskQueue);
    }

    @Override
    public Optional<TaskQueue> getByName(String name) {
        return taskQueueRepository.findByName(name);
    }

    @Override
    public TaskQueueUpdateDTO getForUpdate(Long id) {
        TaskQueue task = findById(id).orElseThrow(() -> new NotFoundException("Not found task queue")); // throw not found exception

        TaskQueueUpdateDTO taskQueueToUpdate = taskQueueMapper.toTaskQueueUpdate(task);

        Department department = task.getDepartment();

        if (department != null) {
            taskQueueToUpdate.setDepartmentName(department.getName());
        }

        return taskQueueToUpdate;
    }

    @Override
    public TaskQueuePagingResponseDTO getAllPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Page<TaskQueue> page = getPage(pageNumber, pageSize, sortBy, sortDir);
        return TaskQueuePagingResponseDTO.builder()
                .currentPage(pageNumber)
                .totalPages(page.getTotalPages())
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .reverseSortDir(sortDir.equals("asc") ? "desc" : "asc")
                .items(taskQueueMapper.toTaskQueueDetailsList(page.getContent()))
                .build();
    }

    @Override
    public TaskQueuePagingResponseDTO getAllUserQueuesPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, User user) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);

        Page<TaskQueue> page = taskQueueRepository.findTaskQueuesByUsersIn(List.of(user),paging);
        return TaskQueuePagingResponseDTO.builder()
                .currentPage(pageNumber)
                .totalPages(page.getTotalPages())
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .reverseSortDir(sortDir.equals("asc") ? "desc" : "asc")
                .items(taskQueueMapper.toTaskQueueDetailsList(page.getContent()))
                .build();
    }

    @Override
    public List<String> findAllNames() {
        return taskQueueRepository.findAllNames();
    }



    private Page<TaskQueue> getPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);
        return taskQueueRepository.findAll(paging);
    }
}
