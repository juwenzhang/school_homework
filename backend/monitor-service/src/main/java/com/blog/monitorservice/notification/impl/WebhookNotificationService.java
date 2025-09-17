package com.blog.monitorservice.notification.impl;

import com.blog.monitorservice.alert.model.AlertMessage;
import com.blog.monitorservice.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Webhook通知服务实现类
 */
@Component
public class WebhookNotificationService implements NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(WebhookNotificationService.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${monitor.notification.webhook.url}")
    private String webhookUrl;
    
    @Value("${monitor.notification.webhook.enabled:false}")
    private boolean enabled;

    @Override
    public boolean sendNotification(AlertMessage alertMessage) {
        if (!enabled) {
            logger.info("Webhook通知功能未启用，跳过发送Webhook请求");
            return false;
        }
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> payload = convertToPayload(alertMessage);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
            
            restTemplate.postForEntity(webhookUrl, request, Map.class);
            logger.info("Webhook通知发送成功: {}", alertMessage.getAlertKey());
            return true;
        } catch (Exception e) {
            logger.error("Webhook通知发送失败: {}", alertMessage.getAlertKey(), e);
            return false;
        }
    }

    @Override
    public String getChannelType() {
        return "WEBHOOK";
    }

    @Override
    public boolean isAvailable() {
        return enabled && webhookUrl != null && !webhookUrl.isEmpty();
    }
    
    /**
     * 将告警消息转换为Webhook载荷
     */
    private Map<String, Object> convertToPayload(AlertMessage alertMessage) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("alertKey", alertMessage.getAlertKey());
        payload.put("alertType", alertMessage.getAlertType());
        payload.put("title", alertMessage.getTitle());
        payload.put("message", alertMessage.getMessage());
        payload.put("severity", alertMessage.getSeverity());
        payload.put("serverIp", alertMessage.getServerIp());
        payload.put("timestamp", alertMessage.getTimestamp());
        
        if (alertMessage.getAdditionalInfo() != null) {
            payload.put("additionalInfo", alertMessage.getAdditionalInfo());
        }
        
        return payload;
    }
}