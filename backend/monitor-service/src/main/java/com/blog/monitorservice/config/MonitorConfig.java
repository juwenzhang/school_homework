package com.blog.monitorservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ConfigurationProperties(prefix = "monitor")
public class MonitorConfig {

    private Alert alert = new Alert();
    private Metrics metrics = new Metrics();

    public static class Alert {
        private boolean enabled = true;
        private Threshold threshold = new Threshold();
        private Notification notification = new Notification();

        // getters and setters
        public boolean isEnabled() {
            return enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        public Threshold getThreshold() {
            return threshold;
        }
        public void setThreshold(Threshold threshold) {
            this.threshold = threshold;
        }
        public Notification getNotification() {
            return notification;
        }
        public void setNotification(Notification notification) {
            this.notification = notification;
        }
    }

    public static class Threshold {
        private int cpu = 80;
        private int memory = 80;
        private int disk = 90;

        // getters and setters
        public int getCpu() {
            return cpu;
        }
        public void setCpu(int cpu) {
            this.cpu = cpu;
        }
        public int getMemory() {
            return memory;
        }
        public void setMemory(int memory) {
            this.memory = memory;
        }
        public int getDisk() {
            return disk;
        }
        public void setDisk(int disk) {
            this.disk = disk;
        }
    }

    public static class Notification {
        private Email email = new Email();
        private Webhook webhook = new Webhook();

        // getters and setters
        public Email getEmail() {
            return email;
        }
        public void setEmail(Email email) {
            this.email = email;
        }
        public Webhook getWebhook() {
            return webhook;
        }
        public void setWebhook(Webhook webhook) {
            this.webhook = webhook;
        }
    }

    public static class Email {
        private boolean enabled = false;
        private String recipients = "admin@example.com";

        // getters and setters
        public boolean isEnabled() {
            return enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        public String getRecipients() {
            return recipients;
        }
        public void setRecipients(String recipients) {
            this.recipients = recipients;
        }
    }

    public static class Webhook {
        private boolean enabled = false;
        private String url = "https://example.com/webhook";

        // getters and setters
        public boolean isEnabled() {
            return enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Metrics {
        private Collection collection = new Collection();
        private Retention retention = new Retention();

        // getters and setters
        public Collection getCollection() {
            return collection;
        }
        public void setCollection(Collection collection) {
            this.collection = collection;
        }
        public Retention getRetention() {
            return retention;
        }
        public void setRetention(Retention retention) {
            this.retention = retention;
        }
    }

    public static class Collection {
        private String interval = "30s";
        private boolean enabled = true;

        // getters and setters
        public String getInterval() {
            return interval;
        }
        public void setInterval(String interval) {
            this.interval = interval;
        }
        public boolean isEnabled() {
            return enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class Retention {
        private int days = 7;

        // getters and setters
        public int getDays() {
            return days;
        }
        public void setDays(int days) {
            this.days = days;
        }
    }

    // getters and setters
    public Alert getAlert() {
        return alert;
    }
    public void setAlert(Alert alert) {
        this.alert = alert;
    }
    public Metrics getMetrics() {
        return metrics;
    }
    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    /**
     * 配置任务调度器，用于定期收集指标
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("monitor-scheduler-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);
        return scheduler;
    }
}