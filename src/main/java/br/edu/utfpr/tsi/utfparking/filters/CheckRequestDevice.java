package br.edu.utfpr.tsi.utfparking.filters;

import br.edu.utfpr.tsi.utfparking.models.entities.ApplicationConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CheckRequestDevice extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        var uri = request.getRequestURI();

        if (uri.equals("/send/plate")) {
            var remoteAddr = request.getRemoteAddr();
            var config = (ApplicationConfig) request.getServletContext().getAttribute("config");

            if (remoteAddr.equals(config.getIp())) {
                chain.doFilter(request, response);
            } else {
                response.addHeader("Content-Type","application/json");
                response.getWriter().print("{\"message\":\"access denied\"}");
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
