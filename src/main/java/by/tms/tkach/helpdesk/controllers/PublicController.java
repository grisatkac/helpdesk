package by.tms.tkach.helpdesk.controllers;

import by.tms.tkach.helpdesk.dto.user.request.UserCreateRequestDTO;
import by.tms.tkach.helpdesk.mappers.UserMapper;
import by.tms.tkach.helpdesk.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class PublicController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /*@GetMapping("/register")
    public String register() {
        return "register";
    }*/

    @ModelAttribute("user")
    public UserCreateRequestDTO getUserCreate() {
        return new UserCreateRequestDTO();
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("register", new UserCreateRequestDTO());
        return "/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user")  UserCreateRequestDTO userCreate, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.validateUserForRegistration(userCreate, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userCreate);
            return "register";
        }

        userService.registerUser(userCreate);
        model.addAttribute("success", "Success registration!");
        return "redirect:/login";
    }
}
