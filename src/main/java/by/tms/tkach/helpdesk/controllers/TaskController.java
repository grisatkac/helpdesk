package by.tms.tkach.helpdesk.controllers;

import by.tms.tkach.helpdesk.dto.form.DefaultDataTaskInputFields;
import by.tms.tkach.helpdesk.dto.task.TaskUpdateDTO;
import by.tms.tkach.helpdesk.dto.task.request.TaskCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.task.response.TaskUpdateResponseDTO;
import by.tms.tkach.helpdesk.dto.user.TaskPagingResponseDTO;
import by.tms.tkach.helpdesk.entities.Task;
import by.tms.tkach.helpdesk.services.TaskService;
import by.tms.tkach.helpdesk.services.validators.TaskValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Service
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskValidator taskValidator;

    @ModelAttribute("taskCreate")
    public TaskCreateRequestDTO getTaskCreate() {
        return TaskCreateRequestDTO.builder().build();
    }

    @GetMapping("/create")
    public String getCreateTaskPage(Model model) {
        DefaultDataTaskInputFields defaultDataTaskInputFields = taskValidator.prepareDefaultDataForTaskFields();
        model.addAttribute("data", defaultDataTaskInputFields);
        return "task/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute TaskCreateRequestDTO taskCreateRequestDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "task/create";
        }

        Task task = taskValidator.validateAndPrepareTaskFotCreate(taskCreateRequestDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "task/create";
        }

        taskService.create(task);
        model.addAttribute("success", "Task created. Wait response from executable user...");
        return "task/create";
    }

    @GetMapping("/{id}")
    public String getTaskDetailsPageById(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.findTaskDetails(id));
        return "task/taskInfo";
    }

    @GetMapping("/update/{id}")
    public String getUpdateTaskPage(@PathVariable Long id, Model model) {
        TaskUpdateResponseDTO forUpdate = taskService.getForUpdate(id);

        forUpdate.setData(taskValidator.prepareDefaultDataForTaskFields());

        model.addAttribute("task", forUpdate);
        return "task/taskUpdate";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,@Valid @ModelAttribute TaskUpdateDTO taskUpdate, Model model, BindingResult bindingResult) {
        TaskUpdateResponseDTO forUpdate = TaskUpdateResponseDTO.builder()
                .taskUpdate(taskUpdate)
                .data(taskValidator.prepareDefaultDataForTaskFields())
                .build();


        if (bindingResult.hasErrors()) {
            model.addAttribute("task", forUpdate);
            return "task/taskUpdate";
        }

        Task task = taskValidator.validateAndPrepareTaskForUpdate(taskUpdate, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("task", forUpdate);
            return "task/taskUpdate";
        }

        taskService.update(task);

        model.addAttribute("task", forUpdate);
        return "task/taskUpdate";
    }

    @GetMapping("/all")
    public String findAll(Model model,
                          @RequestParam(defaultValue = "0") Integer pageNumber,
                          @RequestParam(defaultValue = "5") Integer pageSize,
                          @RequestParam(defaultValue = "createdDate") String sortBy,
                          @RequestParam(defaultValue = "desc") String sortDir)
    {
        TaskPagingResponseDTO response = taskService.getAllPageable(pageNumber, pageSize, sortBy, sortDir);
        model.addAttribute("page", response);
        return "task/tasks";
    }
    @GetMapping("/all/{filter}")
    public String findAllByFilter(Model model,
                          @RequestParam(defaultValue = "0") Integer pageNumber,
                          @RequestParam(defaultValue = "5") Integer pageSize,
                          @RequestParam(defaultValue = "createdDate") String sortBy,
                          @RequestParam(defaultValue = "desc") String sortDir,
                          @PathVariable String filter,
                          HttpServletRequest httpServletRequest)
    {
        String a = filter;
        String requestURI = httpServletRequest.getRequestURI();
        //TaskPagingResponseDTO response = taskService.getAllPageable(pageNumber, pageSize, sortBy, sortDir);
        TaskPagingResponseDTO response = taskService.getAllPageableByFilter(pageNumber, pageSize, sortBy, sortDir, filter, httpServletRequest.getRequestURI());
        model.addAttribute("page", response);
        return "task/tasks";
    }

    @GetMapping("/{id}/action/{operation}")
    public String makeOperationWithTask(@PathVariable Long id, @PathVariable String operation, HttpServletRequest httpServletRequest) {
        taskValidator.prepareAndInteractWithTask(operation, id);
        String a = "redirect:" + httpServletRequest.getRequestURI();
        return "redirect:/tasks/all";
    }
}
