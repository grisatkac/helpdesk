package by.tms.tkach.helpdesk.controllers;

import by.tms.tkach.helpdesk.dto.taskQueue.TaskQueueUpdateDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.request.TaskQueueCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueuePagingResponseDTO;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.services.impl.TaskQueueServiceImpl;
import by.tms.tkach.helpdesk.services.validators.TaskQueueValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Service
@AllArgsConstructor
@RequestMapping("/taskQueues")
public class TaskQueueController {

    private final TaskQueueServiceImpl taskQueueService;
    private final TaskQueueValidator taskQueueValidator;

    @ModelAttribute("taskQueueCreateRequest")
    public TaskQueueCreateRequestDTO getTaskQueueCreateRequestModel() {
        return new TaskQueueCreateRequestDTO();
    }

    @ModelAttribute("taskQueueUpdate")
    public TaskQueueUpdateDTO TaskQueueUpdateDTO() {
        return TaskQueueUpdateDTO.builder().build();
    }

    @GetMapping("/create")
    public String getCreateTaskQueuePage() {
        return "taskQueue/create";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute("taskQueue", taskQueueService.getTaskQueueDetails(id));
        return "taskQueue/taskQueueInfo";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute TaskQueueCreateRequestDTO createRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "taskQueue/create";
        }

        taskQueueValidator.validateAndPrepareForCreate(createRequestDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "taskQueue/create";
        }

        return "redirect:/taskQueues/create";
    }

    @GetMapping("/update/{id}")
    public String getByIdForUpdatePage(@PathVariable Long id, Model model) {
        //TaskQueueDetailsResponseDTO taskQueueToUpdate = taskQueueService.findById(id);
        TaskQueueUpdateDTO taskQueueToUpdate = taskQueueService.getForUpdate(id);
        model.addAttribute("taskQueueToUpdate", taskQueueToUpdate);
        return "taskQueue/taskQueueUpdate";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute TaskQueueUpdateDTO taskQueueUpdate, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("taskQueueToUpdate",taskQueueService.getForUpdate(id));
            return "taskQueue/taskQueueUpdate";
        }

        TaskQueue taskQueue = taskQueueValidator.validateAndPrepareForUpdate(taskQueueUpdate, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("taskQueueToUpdate",taskQueueService.getForUpdate(id));
            return "taskQueue/taskQueueUpdate";
        }

        taskQueueService.update(taskQueue);
        model.addAttribute("taskQueueToUpdate", taskQueueUpdate);
        model.addAttribute("success", "success");
        return "taskQueue/taskQueueUpdate";
    }

    @GetMapping("/all")
    public String findAll(Model model,
                          @RequestParam(defaultValue = "0") Integer pageNumber,
                          @RequestParam(defaultValue = "5") Integer pageSize,
                          @RequestParam(defaultValue = "name") String sortBy,
                          @RequestParam(defaultValue = "asc") String sortDir)
    {


        TaskQueuePagingResponseDTO response = taskQueueService.getAllPageable(pageNumber, pageSize, sortBy, sortDir);
        model.addAttribute("page", response);
        return "taskQueue/taskQueues";
    }
}
