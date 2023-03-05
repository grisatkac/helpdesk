package by.tms.tkach.helpdesk.config.security;


import by.tms.tkach.helpdesk.dto.auth.AuthUser;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        //AuthUser userForAuthContext = userService.getUserForAuthContext(login);
        User userForAuthContext = userService.getByLogin(login);

        return new CustomUserDetails(userForAuthContext);
    }
}
