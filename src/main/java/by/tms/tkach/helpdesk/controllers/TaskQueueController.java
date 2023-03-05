package by.tms.tkach.helpdesk.controllers;

import by.tms.tkach.helpdesk.dto.taskQueue.TaskQueueUpdateDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.request.TaskQueueCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueuePagingResponseDTO;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.entities.enums.task.Roles;
import by.tms.tkach.helpdesk.mappers.TaskQueueMapper;
import by.tms.tkach.helpdesk.mappers.UserMapper;
import by.tms.tkach.helpdesk.services.impl.TaskQueueServiceImpl;
import by.tms.tkach.helpdesk.services.validators.TaskQueueValidator;
import by.tms.tkach.helpdesk.utils.AuthUserUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.extras.springsecurity5.auth.AuthUtils;

import javax.validation.Valid;

@Service
@AllArgsConstructor
@RequestMapping("/taskQueues")
public class TaskQueueController {

    private final TaskQueueServiceImpl taskQueueService;
    private final TaskQueueMapper taskQueueMapper;
    private final TaskQueueValidator taskQueueValidator;

    @ModelAttribute("queueToCreate")
    public TaskQueueCreateRequestDTO getTaskQueueCreateRequestModel() {
        return new TaskQueueCreateRequestDTO();
    }

    @ModelAttribute("queue")
    public TaskQueueUpdateDTO TaskQueueUpdateDTO() {
        return TaskQueueUpdateDTO.builder().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String getCreateTaskQueuePage(@ModelAttribute("queueToCreate") TaskQueueCreateRequestDTO createRequestDTO) {
        return "taskQueue/create";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute("taskQueue", taskQueueService.getTaskQueueDetails(id));
        return "taskQueue/taskQueueInfo";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("queueToCreate") TaskQueueCreateRequestDTO createRequestDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("queueToCreate", createRequestDTO);
            return "taskQueue/create";
        }

        taskQueueValidator.validateQueueToCreate(taskQueueMapper.toTaskQueue(createRequestDTO), bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("queueToCreate", createRequestDTO);
            return "taskQueue/create";
        }

        taskQueueService.create(taskQueueMapper.toTaskQueue(createRequestDTO));

        return "redirect:/taskQueues/all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/update/{id}")
    public String getByIdForUpdatePage(@ModelAttribute("queue") TaskQueueUpdateDTO taskQueueUpdate, @PathVariable Long id, Model model) {
        TaskQueueUpdateDTO taskQueueToUpdate = taskQueueService.getForUpdate(id);
        model.addAttribute("queue", taskQueueToUpdate);
        return "taskQueue/taskQueueUpdate";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("queue") TaskQueueUpdateDTO taskQueueUpdate, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            //model.addAttribute("queue",taskQueueService.getForUpdate(id));
            return "taskQueue/taskQueueUpdate";
        }

        taskQueueValidator.validateQueueToUpdate(taskQueueMapper.toTaskQueue(taskQueueUpdate), bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("queue", taskQueueUpdate);
            return "taskQueue/taskQueueUpdate";
        }

        taskQueueService.update(taskQueueMapper.toTaskQueue(taskQueueUpdate));

        return "redirect:/taskQueues/all";
    }

    @GetMapping("/all")
    public String findAll(Model model,
                          @RequestParam(defaultValue = "0") Integer pageNumber,
                          @RequestParam(defaultValue = "5") Integer pageSize,
                          @RequestParam(defaultValue = "name") String sortBy,
                          @RequestParam(defaultValue = "asc") String sortDir)
    {
        TaskQueuePagingResponseDTO response = null;

        if (AuthUserUtils.getAuthUser().getRole().name().equals(Roles.ROLE_ADMIN.name())) {
            response = taskQueueService.getAllPageable(pageNumber, pageSize, sortBy, sortDir);
        }

        if (AuthUserUtils.getAuthUser().getRole().name().equals(Roles.ROLE_USER.name())) {
            response = taskQueueService.getAllUserQueuesPageable(pageNumber, pageSize, sortBy, sortDir, AuthUserUtils.getAuthUser() /*userMapper.toUser(AuthUserUtils.getAuthUser())*/);
        }

        model.addAttribute("page", response);
        return "taskQueue/taskQueues";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        taskQueueService.delete(id);
        return "redirect:/taskQueues/all";
    }
}
