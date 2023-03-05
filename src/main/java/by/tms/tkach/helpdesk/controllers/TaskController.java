package by.tms.tkach.helpdesk.controllers;

import by.tms.tkach.helpdesk.dto.form.DefaultDataTaskInputFields;
import by.tms.tkach.helpdesk.dto.task.TaskUpdateDTO;
import by.tms.tkach.helpdesk.dto.task.request.TaskCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.task.response.TaskUpdateResponseDTO;
import by.tms.tkach.helpdesk.dto.task.TaskPagingResponseDTO;
import by.tms.tkach.helpdesk.mappers.TaskMapper;
import by.tms.tkach.helpdesk.services.TaskService;
import by.tms.tkach.helpdesk.services.validators.TaskValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final TaskMapper taskMapper;
    private final TaskValidator taskValidator;

    @ModelAttribute("taskToCreate")
    public TaskCreateRequestDTO getTaskCreate() {
        return TaskCreateRequestDTO.builder().build();
    }

    @ModelAttribute("taskToUpdate")
    public TaskUpdateDTO  getTaskUpdateResponseDTO() {
        return TaskUpdateDTO.builder().build();
    }

    @GetMapping("/create")
    public String getCreateTaskPage(@ModelAttribute("taskToCreate") TaskCreateRequestDTO taskCreate, Model model) {
        DefaultDataTaskInputFields defaultDataTaskInputFields = taskValidator.prepareDefaultDataForTaskFields();
        model.addAttribute("data", defaultDataTaskInputFields);
        return "task/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("taskToCreate") @Valid TaskCreateRequestDTO taskCreateRequestDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            DefaultDataTaskInputFields defaultDataTaskInputFields = taskValidator.prepareDefaultDataForTaskFields();
            model.addAttribute("data", defaultDataTaskInputFields);
            model.addAttribute("taskToCreate", taskCreateRequestDTO);
            return "task/create";
        }

        taskValidator.validateToCreate(taskMapper.toTask(taskCreateRequestDTO), bindingResult);

        if (bindingResult.hasErrors()) {
            DefaultDataTaskInputFields defaultDataTaskInputFields = taskValidator.prepareDefaultDataForTaskFields();
            model.addAttribute("data", defaultDataTaskInputFields);
            model.addAttribute("taskToCreate", taskCreateRequestDTO);
            return "task/create";
        }

        taskService.create(taskMapper.toTask(taskCreateRequestDTO));
        return "redirect:/tasks/all/assignable";
    }

    @GetMapping("/{id}")
    public String getTaskDetailsPageById( @PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.findTaskDetails(id));
        return "task/taskInfo";
    }

    @GetMapping("/update/{id}")
    public String getUpdateTaskPage(@ModelAttribute("taskToUpdate") TaskUpdateDTO taskUpdateDTO, @PathVariable Long id, Model model) {
        TaskUpdateDTO forUpdate = taskService.getForUpdate(id);

        forUpdate.setData(taskValidator.prepareDefaultDataForTaskFields());

        model.addAttribute("taskToUpdate", forUpdate);
        return "task/taskUpdate";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,@Valid @ModelAttribute("taskToUpdate") TaskUpdateDTO taskUpdate, Model model, BindingResult bindingResult) {
        DefaultDataTaskInputFields defaultDataTaskInputFields = taskValidator.prepareDefaultDataForTaskFields();
        taskUpdate.setData(defaultDataTaskInputFields);
        if (bindingResult.hasErrors()) {
            model.addAttribute("taskToUpdate", taskUpdate);
            return "task/taskUpdate";
        }

        taskValidator.validateToUpdate(taskMapper.toTask(taskUpdate), bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("taskToUpdate", taskUpdate);
            return "task/taskUpdate";
        }

        taskService.update(taskMapper.toTask(taskUpdate));

        return "redirect:/tasks/all/assignable";
    }

    @GetMapping("/all")
    public String findAll(Model model,
                          @RequestParam(defaultValue = "0") Integer pageNumber,
                          @RequestParam(defaultValue = "5") Integer pageSize,
                          @RequestParam(defaultValue = "createdDate") String sortBy,
                          @RequestParam(defaultValue = "desc") String sortDir,
                          HttpServletRequest httpServletRequest)
    {
        String requestURI = httpServletRequest.getRequestURI();
        TaskPagingResponseDTO response = taskService.getAllPageable(pageNumber, pageSize, sortBy, sortDir, requestURI);
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
        TaskPagingResponseDTO response = taskService.getAllPageableByFilter(pageNumber, pageSize, sortBy, sortDir, filter, httpServletRequest.getRequestURI());
        model.addAttribute("page", response);
        return "task/tasks";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        taskService.delete(id);
        return "redirect:/tasks/all/assignable";
    }
}
