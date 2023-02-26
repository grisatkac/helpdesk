package by.tms.tkach.helpdesk.services.impl;

import by.tms.tkach.helpdesk.dto.taskQueue.TaskQueueUpdateDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueueDetailsResponse;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueuePagingResponseDTO;
import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.exception.EntityNotFound;
import by.tms.tkach.helpdesk.mappers.TaskQueueMapper;
import by.tms.tkach.helpdesk.repositories.TaskQueueRepository;
import by.tms.tkach.helpdesk.services.TaskQueueService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskQueueServiceImpl implements TaskQueueService {

    private final TaskQueueRepository taskQueueRepository;
    private final TaskQueueMapper taskQueueMapper;

    @Override
    public TaskQueue create(TaskQueue entity) {
        return null;
    }

    @Override
    public TaskQueue findById(Long id) {
        return taskQueueRepository.findById(id).orElseThrow(() -> new EntityNotFound("cant find"));
    }

    @Override
    public List<TaskQueue> getAll() {
        return null;
    }

    @Override
    public TaskQueue update(TaskQueue taskQueue) {
        return taskQueueRepository.save(taskQueue);
    }

    @Override
    public TaskQueue delete(Long id) {
        return null;
    }

    @Override
    public TaskQueueDetailsResponse getTaskQueueDetails(Long id) {
        TaskQueue foundTaskQueue = taskQueueRepository.findTaskQueueDetails(id);
        return taskQueueMapper.toTaskDetails(foundTaskQueue);
    }

    @Override
    public TaskQueue getByName(String name) {
        return taskQueueRepository.findByName(name);
    }

    @Override
    public TaskQueueUpdateDTO getForUpdate(Long id) {
        TaskQueue task = findById(id); // throw not found exception

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
    public List<String> findAllNames() {
        return taskQueueRepository.findAllNames();
    }

    private Page<TaskQueue> getPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);
        return taskQueueRepository.findAll(paging);
    }
}
