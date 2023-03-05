package by.tms.tkach.helpdesk.controllers;

import by.tms.tkach.helpdesk.dto.user.UserQueuesDTO;
import by.tms.tkach.helpdesk.dto.user.UserUpdateDTO;
import by.tms.tkach.helpdesk.dto.user.response.UserPagingResponseDTO;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.mappers.UserMapper;
import by.tms.tkach.helpdesk.services.UserService;
import by.tms.tkach.helpdesk.services.validators.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @ModelAttribute("userQueue")
    public UserQueuesDTO getUserQueue() {
        return UserQueuesDTO.builder().build();
    }

    @ModelAttribute("userToUpdate")
    public UserUpdateDTO getUpdateUser() {
        return UserUpdateDTO.builder().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user details data")
    public String getById(@PathVariable Long id, Model model) {
        User foundUser = userService.getUserDetails(id);
        model.addAttribute("user", userMapper.toUserDetails(foundUser));
        return "user/userInfo";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/taskQueues")
    public String getUserTasksAddOrDeletePage(@PathVariable Long id, Model model) {
        model.addAttribute("userQueue", UserQueuesDTO.builder().id(id).build());
        return "user/queuesManage";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/taskQueues/add")
    public String addUserQueue(@PathVariable Long id, @Valid @ModelAttribute UserQueuesDTO userQueuesDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "user/queuesManage";
        }

        userService.addTaskQueue(userQueuesDTO.getQueueName(), id);

        return String.format("redirect:/users/%s/taskQueues", id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/taskQueues/delete")
    public String deleteUserQueue(@PathVariable Long id, @Valid @ModelAttribute UserQueuesDTO userQueuesDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/queuesManage";
        }

        userService.removeTaskQueue(userQueuesDTO.getQueueName(),id);

        return String.format("redirect:/users/%s/taskQueues", id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/update/{id}")
    public String getTaskForUpdate(@ModelAttribute("userToUpdate") UserUpdateDTO userUpdateDTO, @PathVariable Long id, Model model) {
        UserUpdateDTO forUpdate = userService.getForUpdate(id);

        model.addAttribute("userToUpdate", forUpdate);
        return "user/userUpdate";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("userToUpdate") UserUpdateDTO userUpdateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userToUpdate", userUpdateDTO);
            return "user/userUpdate";
        }

        userValidator.validateToUpdate(userMapper.toUser(userUpdateDTO), bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("userToUpdate", userUpdateDTO);
            return "user/userUpdate";
        }

        userService.update(userMapper.toUser(userUpdateDTO));
        return "user/userUpdate";
    }

    @GetMapping("/all")
    public String findAll(Model model,
                          @RequestParam(defaultValue = "0") Integer pageNumber,
                          @RequestParam(defaultValue = "5") Integer pageSize,
                          @RequestParam(defaultValue = "firstName") String sortBy,
                          @RequestParam(defaultValue = "asc") String sortDir)
    {
        UserPagingResponseDTO response = userService.getAllPageable(pageNumber, pageSize, sortBy, sortDir);
        model.addAttribute("page", response);
        return "user/users";
    }

    @GetMapping("/task/{id}/{operation}")
    public String makeOperationWithTask(@PathVariable Long id, @PathVariable String operation, HttpServletRequest httpServletRequest) {
        userService.interactWithTask(operation, id);
        return "redirect:/tasks/all/executable";
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/users/all";
    }
}
