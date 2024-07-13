package com.lets.kkiri.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.common.exception.KkiriException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerFilter.class);

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        try {
            filterChain.doFilter(request, response);
        } catch (KkiriException e) {
            response.setStatus(e.getErrorCode().getStatus().value());
            response.setContentType("application/json");

            Map<String, Object> errorAttributes = new LinkedHashMap<>();
            SimpleDateFormat jsonFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date now = new Date();
            logger.info("[" + e.getErrorCode().getStatus().value() + e.getErrorCode().getStatus().getReasonPhrase() + " " +  request.getRequestURI() + "] : " + e.getMessage());
            errorAttributes.put("timestamp", jsonFormat.format(now));
            errorAttributes.put("status", e.getErrorCode().getStatus().value());
            errorAttributes.put("error", e.getErrorCode().getStatus().getReasonPhrase());
            errorAttributes.put("message", e.getMessage());
            errorAttributes.put("path", request.getRequestURI());


            response.getWriter().write(new ObjectMapper().writeValueAsString(errorAttributes));
        }
    }
}
