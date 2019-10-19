package br.edu.utfpr.tsi.utfparking.filters;

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
        var remoteAddr = request.getRemoteAddr();

        System.out.println(remoteAddr);

        chain.doFilter(request, response);
    }
}
