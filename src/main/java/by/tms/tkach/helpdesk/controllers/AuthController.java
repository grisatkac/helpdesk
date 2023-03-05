package by.tms.tkach.helpdesk.controllers;

import by.tms.tkach.helpdesk.dto.auth.AuthRequest;
import by.tms.tkach.helpdesk.dto.auth.AuthToken;
import by.tms.tkach.helpdesk.dto.auth.RegistrationRequest;
import by.tms.tkach.helpdesk.dto.user.request.UserCreateRequestDTO;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.mappers.UserMapper;
import by.tms.tkach.helpdesk.services.UserService;
import by.tms.tkach.helpdesk.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping("/rest/login")
    public AuthToken auth(@RequestBody AuthRequest authRequest) {
        User user = userService.findByLoginAndPassword(authRequest.getUserName(), authRequest.getPassword());
        String token = JwtUtils.generateToken(user.getLogin());
        return AuthToken.builder().token(token).build();
    }

    @PostMapping("/rest/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserCreateRequestDTO registrationRequest) {
        User user = userMapper.toUser(registrationRequest);
        userService.create(user);
    }

}
