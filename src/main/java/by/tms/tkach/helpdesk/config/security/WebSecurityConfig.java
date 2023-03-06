package by.tms.tkach.helpdesk.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@AllArgsConstructor
@Profile("form")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic()
                .and()
                .csrf()
                .disable()
                .cors().configurationSource(configurationSource())
                .and()
                .authorizeRequests()
                .antMatchers("/", "/static/**", "/css/**", "/vendor/**", "/js/**",  "/register"/*, "/swagger-ui/**", "/rest/**", "/v3/api-docs/**"*/).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().usernameParameter("login").loginPage("/login").defaultSuccessUrl("/main").permitAll()
                .and()
                .logout()
                //.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                //.invalidateHttpSession(true)
                //.clearAuthentication(true)
                //.deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/").permitAll()
                .and().build();
    }

    private CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedHeader("*");
        configuration.setAllowedHeaders(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
