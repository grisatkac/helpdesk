package by.tms.tkach.helpdesk.controllers;

import by.tms.tkach.helpdesk.dto.user.response.UserDetailsResponse;
import by.tms.tkach.helpdesk.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user details data")
    public String getById(@PathVariable Long id, Model model) {
        UserDetailsResponse foundUser = userService.getUserDetails(id);
        model.addAttribute("user", foundUser);
        return "user/userInfo";
    }
}
