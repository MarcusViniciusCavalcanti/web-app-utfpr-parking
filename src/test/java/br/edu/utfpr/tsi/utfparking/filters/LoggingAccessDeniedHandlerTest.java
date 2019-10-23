package br.edu.utfpr.tsi.utfparking.filters;

import org.eclipse.jetty.util.IO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LoggingAccessDeniedHandlerTest {

    @MockBean
    private HttpServletRequest request;

    @MockBean
    private HttpServletResponse response;

    @MockBean
    private AccessDeniedException accessDeniedException;

    @MockBean
    private SecurityContext securityContext;

    @MockBean
    private Authentication authentication;

    @Test
    public void should_redirect_when_is_authenticated() throws IOException {
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(accessDeniedException.getMessage()).thenReturn("message exception");
        Mockito.when(authentication.getName()).thenReturn("name");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        var loggingAccessDeniedHandler = new LoggingAccessDeniedHandler();

        loggingAccessDeniedHandler.handle(request, response, accessDeniedException);

        Mockito.verify(response, Mockito.times(1)).sendRedirect(Mockito.contains("/access-denied"));
    }

    @Test
    public void should_redirect_when_is_unauthenticated() throws IOException {
        Mockito.when(accessDeniedException.getMessage()).thenReturn("message exception");
        Mockito.when(authentication.getName()).thenReturn("name");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        var loggingAccessDeniedHandler = new LoggingAccessDeniedHandler();

        loggingAccessDeniedHandler.handle(request, response, accessDeniedException);

        Mockito.verify(response, Mockito.times(1)).sendRedirect(Mockito.contains("/access-denied-public"));
    }
}
