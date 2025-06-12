package org.example.expert.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
public class AdminApiLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(AdminApiLoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || " +
            "execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public Object logAdminApi(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //API 요청 시각
        LocalDateTime requestTime = LocalDateTime.now();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String userId = request.getHeader("X-Forwarded-For"); // 요청한 사용자의 ID
        String uri = request.getRequestURI(); //API 요청 URL

        // 요청 본문
        String requestBody = objectMapper.writeValueAsString(proceedingJoinPoint.getArgs());

        log.info("ADMIN API REQUEST : userId={}, URL={}, Time={}, RequestBody={}",
                userId, uri, requestTime, requestBody);

        Object returnObj = proceedingJoinPoint.proceed();

        // 응답 본문
        String responseBody = objectMapper.writeValueAsString(returnObj);

        log.info("ADMIN API RESPONSE : userId={}, URL={}, ResponseBody={}", userId, uri, responseBody);

        return returnObj;
    }
}
