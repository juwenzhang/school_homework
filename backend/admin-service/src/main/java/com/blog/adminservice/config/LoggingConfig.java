package com.blog.adminservice.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;

@Aspect
@Component
public class LoggingConfig {

    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);

    /**
     * 定义切点，拦截所有Controller方法
     */
    @Pointcut("execution(* com.blog.adminservice.controller.*.*(..))")
    public void controllerPointcut() {}

    /**
     * 环绕通知，记录请求和响应日志
     */
    @Around("controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // 记录请求日志
        logger.info("Request: {}", request.getRequestURL().toString());
        logger.info("HTTP Method: {}", request.getMethod());
        logger.info("IP: {}", request.getRemoteAddr());
        logger.info("Class Method: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), method.getName());
        logger.info("Request Args: {}", Arrays.toString(joinPoint.getArgs()));
        
        // 记录请求头
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            logger.debug("Header: {}={}", headerName, headerValue);
        }
        
        // 记录请求时间
        long startTime = System.currentTimeMillis();
        
        // 执行原方法
        Object result = joinPoint.proceed();
        
        // 记录响应时间和结果
        long endTime = System.currentTimeMillis();
        logger.info("Response: {}", result);
        logger.info("Response Time: {} ms", (endTime - startTime));
        
        return result;
    }
}