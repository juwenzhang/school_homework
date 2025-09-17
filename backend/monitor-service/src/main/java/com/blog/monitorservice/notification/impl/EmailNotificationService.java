package com.blog.monitorservice.notification.impl;

import com.blog.monitorservice.alert.model.AlertMessage;
import com.blog.monitorservice.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * 邮件通知服务实现类
 */
@Component
public class EmailNotificationService implements NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${monitor.notification.email.from}")
    private String fromEmail;
    
    @Value("${monitor.notification.email.to}")
    private String[] toEmails;
    
    @Value("${monitor.notification.email.enabled:false}")
    private boolean enabled;

    @Override
    public boolean sendNotification(AlertMessage alertMessage) {
        if (!enabled) {
            logger.info("邮件通知功能未启用，跳过发送邮件");
            return false;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmails);
            message.setSubject("【" + alertMessage.getSeverity() + "】" + alertMessage.getTitle());
            message.setText(buildEmailContent(alertMessage));
            
            mailSender.send(message);
            logger.info("邮件通知发送成功: {}", alertMessage.getAlertKey());
            return true;
        } catch (MailException e) {
            logger.error("邮件通知发送失败: {}", alertMessage.getAlertKey(), e);
            return false;
        }
    }

    @Override
    public String getChannelType() {
        return "EMAIL";
    }

    @Override
    public boolean isAvailable() {
        return enabled;
    }
    
    /**
     * 构建邮件内容
     */
    private String buildEmailContent(AlertMessage alertMessage) {
        StringBuilder content = new StringBuilder();
        content.append("告警类型: ").append(alertMessage.getAlertType()).append("\n");
        content.append("严重程度: ").append(alertMessage.getSeverity()).append("\n");
        content.append("告警消息: ").append(alertMessage.getMessage()).append("\n");
        content.append("服务器IP: ").append(alertMessage.getServerIp()).append("\n");
        content.append("告警时间: ").append(new java.util.Date(alertMessage.getTimestamp())).append("\n");
        
        if (alertMessage.getAdditionalInfo() != null && !alertMessage.getAdditionalInfo().isEmpty()) {
            content.append("\n附加信息:\n");
            for (Map.Entry<String, Object> entry : alertMessage.getAdditionalInfo().entrySet()) {
                content.append("  - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        
        return content.toString();
    }
}