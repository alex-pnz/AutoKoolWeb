package com.fullstackhub.autokoolweb.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

import static com.fullstackhub.autokoolweb.constants.StringConstants.ADMIN_QUESTIONS_URL;
import static com.fullstackhub.autokoolweb.constants.StringConstants.USER_VIEW_URL;

@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        boolean hasUserRole = false;
        boolean hasAdminRole = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("USER")) {
                hasUserRole = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ADMIN")) {
                hasAdminRole = true;
                break;
            }
        }

        if (hasUserRole) {
            redirectStrategy.sendRedirect(request, response, USER_VIEW_URL);
        } else if (hasAdminRole) {
            redirectStrategy.sendRedirect(request, response, ADMIN_QUESTIONS_URL);
        } else {
            throw new IllegalStateException();
        }
    }
}
