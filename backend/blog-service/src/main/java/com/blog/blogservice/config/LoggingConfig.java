package com.blog.blogservice.config;

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

/**
 * 日志配置类
 */
@Aspect
@Component
public class LoggingConfig {

    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);

    /**
     * 定义切点，拦截所有Controller方法
     */
    @Pointcut("execution(* com.blog.blogservice.controller.*.*(..))")
    public void logPointCut() {
    }

    /**
     * 环绕通知，用于记录请求和响应日志
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        // 获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        
        // 记录请求信息
        logger.info("请求 URL: {}", request.getRequestURL().toString());
        logger.info("请求方法: {}", request.getMethod());
        logger.info("请求 IP: {}", request.getRemoteAddr());
        logger.info("请求类名: {}, 方法名: {}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());
        logger.info("请求参数: {}", Arrays.toString(point.getArgs()));
        
        // 获取请求头信息
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            logger.debug("请求头: {}={}", headerName, headerValue);
        }
        
        // 执行目标方法
        Object result = point.proceed();
        
        // 记录响应信息
        long endTime = System.currentTimeMillis();
        logger.info("响应结果: {}", result);
        logger.info("请求耗时: {}ms", (endTime - startTime));
        
        return result;
    }
}