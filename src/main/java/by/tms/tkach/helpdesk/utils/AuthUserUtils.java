package by.tms.tkach.helpdesk.utils;

import by.tms.tkach.helpdesk.config.security.CustomUserDetails;
import by.tms.tkach.helpdesk.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthUserUtils {

    private AuthUserUtils() {}

    public static User /*AuthUser*/ getAuthUser() {
        CustomUserDetails auth = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return auth.getUser();
    }

    public static boolean isAuthUserHasDepartment() {
        return getAuthUser().getDepartment() != null;
    }
}
