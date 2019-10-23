package br.edu.utfpr.tsi.utfparking.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class LoggingAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .ifPresentOrElse(
                        auth -> {
                            log.info(
                                    "'{}' was trying to access protected resource: '{}' | exception message: '{}'",
                                    auth.getName(),
                                    request.getRequestURI(),
                                    e.getMessage()
                            );
                            try {
                                response.sendRedirect(request.getContextPath() + "/access-denied");
                            } catch (IOException ex) {
                                log.error("error when send redirect '{}'", ex.getMessage());
                            }
                        },
                        () -> {
                            try {
                                SecurityContextHolder.clearContext();
                                response.sendRedirect(request.getContextPath() + "/access-denied-public");
                            } catch (IOException ex) {
                                log.error("error when send redirect '{}'", ex.getMessage());
                            }
                        }
                );

    }
}
