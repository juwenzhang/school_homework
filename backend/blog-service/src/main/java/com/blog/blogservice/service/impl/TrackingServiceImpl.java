package com.blog.blogservice.service.impl;

import com.blog.blogservice.entity.TrackingEvent;
import com.blog.blogservice.repository.TrackingEventRepository;
import com.blog.blogservice.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 埋点服务实现类
 */
@Service
public class TrackingServiceImpl implements TrackingService {

    @Autowired
    private TrackingEventRepository trackingEventRepository;

    // 浏览器正则表达式
    private static final Pattern BROWSER_PATTERN = Pattern.compile("(Firefox|Chrome|Safari|Edge|Opera)/(\\d+\\.\\d+)");
    // 操作系统正则表达式
    private static final Pattern OS_PATTERN = Pattern.compile("(Windows|Macintosh|Linux|Android|iOS)");

    @Override
    @Transactional
    public void trackEvent(String eventName, Long userId, Long articleId, Map<String, Object> properties) {
        TrackingEvent event = new TrackingEvent();
        event.setEventName(eventName);
        event.setUserId(userId);
        event.setArticleId(articleId);
        event.setEventTime(LocalDateTime.now());
        event.setProperties(properties);

        // 获取请求信息
        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            event.setIpAddress(getClientIp(request));
            String userAgent = request.getHeader("User-Agent");
            event.setUserAgent(userAgent);
            
            // 解析浏览器和操作系统信息
            parseUserAgent(userAgent, event);
        }

        trackingEventRepository.save(event);
    }

    @Override
    @Transactional
    public void trackPageView(String pageUrl, Long userId, Long duration) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("pageUrl", pageUrl);
        properties.put("duration", duration);
        trackEvent("PAGE_VIEW", userId, null, properties);
    }

    @Override
    @Transactional
    public void trackArticleRead(Long articleId, Long userId, Long duration) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("duration", duration);
        trackEvent("ARTICLE_READ", userId, articleId, properties);
    }

    @Override
    public Page<TrackingEvent> getEventsByEventName(String eventName, Pageable pageable) {
        return trackingEventRepository.findByEventName(eventName, pageable);
    }

    @Override
    public Page<TrackingEvent> getEventsByUserId(Long userId, Pageable pageable) {
        return trackingEventRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<TrackingEvent> getEventsByArticleId(Long articleId, Pageable pageable) {
        return trackingEventRepository.findByArticleId(articleId, pageable);
    }

    @Override
    public Page<TrackingEvent> getEventsByTimeRange(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        return trackingEventRepository.findByEventTimeBetween(startTime, endTime, pageable);
    }

    @Override
    public Long countEventsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return trackingEventRepository.countByEventTimeBetween(startTime, endTime);
    }

    @Override
    public Long countEventsByEventNameAndTimeRange(String eventName, LocalDateTime startTime, LocalDateTime endTime) {
        return trackingEventRepository.countByEventNameAndEventTimeBetween(eventName, startTime, endTime);
    }

    @Override
    public Map<Long, Long> getPopularArticles(int limit, int days) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(days);
        
        return new HashMap<>();
    }

    @Override
    public Map<String, Long> getUserActivity(int days) {
        Map<String, Long> activityMap = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (int i = days - 1; i >= 0; i--) {
            LocalDateTime date = now.minusDays(i);
            String dateStr = date.format(formatter);
            LocalDateTime startTime = date.withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endTime = date.withHour(23).withMinute(59).withSecond(59);
            
            Long count = trackingEventRepository.countByEventNameAndEventTimeBetween("PAGE_VIEW", startTime, endTime);
            activityMap.put(dateStr, count);
        }
        
        return activityMap;
    }

    /**
     * 获取当前请求对象
     */
    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest();
            }
        } catch (Exception _) {}
        return null;
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级代理情况下，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 解析User-Agent字符串，提取浏览器和操作系统信息
     */
    private void parseUserAgent(String userAgent, TrackingEvent event) {
        if (userAgent == null) {
            return;
        }

        // 解析浏览器信息
        Matcher browserMatcher = BROWSER_PATTERN.matcher(userAgent);
        if (browserMatcher.find()) {
            event.setBrowser(browserMatcher.group(1) + " " + browserMatcher.group(2));
        }

        // 解析操作系统信息
        Matcher osMatcher = OS_PATTERN.matcher(userAgent);
        if (osMatcher.find()) {
            event.setOs(osMatcher.group(1));
        }
    }
}