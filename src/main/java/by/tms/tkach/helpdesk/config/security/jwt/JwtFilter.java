package by.tms.tkach.helpdesk.config.security.jwt;

import by.tms.tkach.helpdesk.config.security.CustomUserDetails;
import by.tms.tkach.helpdesk.config.security.CustomUserDetailsService;
import by.tms.tkach.helpdesk.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.springframework.util.StringUtils.hasText;

@Component
@AllArgsConstructor
@Profile("jwt")
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (Objects.nonNull(token) && JwtUtils.validateToken(token)) {
            String login = JwtUtils.getLogin(token);
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(login);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    null,
                    customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith(JwtUtils.BEARER)) {
            return bearer.substring(JwtUtils.BEARER.length());
        }
        return null;
    }
}
