package com.blog.userservice.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller日志增强器
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 定义切点，拦截所有Controller方法
     */
    @Pointcut("execution(* com.blog.userservice.controller.*.*(..))")
    public void logPointCut() {
    }

    /**
     * 环绕通知，记录请求和响应信息
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 记录请求开始日志
        logger.info("请求开始: {}", request.getRequestURI());
        logger.info("请求方法: {}", request.getMethod());
        logger.info("请求IP: {}", request.getRemoteAddr());
        logger.info("请求参数: {}", Arrays.toString(point.getArgs()));
        logger.info("请求头信息: {}", getHeadersInfo(request));

        // 记录请求处理时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 执行方法
        Object result = point.proceed();

        // 停止计时
        stopWatch.stop();

        // 记录请求结束日志
        logger.info("请求结束: {}", request.getRequestURI());
        logger.info("处理时间: {}ms", stopWatch.getTotalTimeMillis());
        logger.info("响应结果: {}", result);

        return result;
    }

    /**
     * 获取请求头信息
     */
    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}