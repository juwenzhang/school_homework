package com.blog.monitorservice.alert.model;

import java.io.Serializable;

public class AlertConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean enabled = true;
    private Threshold threshold = new Threshold();
    private Notification notification = new Notification();
    private int alertFrequencyLimit = 10; // 分钟

    // getter和setter方法
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

    public int getAlertFrequencyLimit() {
        return alertFrequencyLimit;
    }

    public void setAlertFrequencyLimit(int alertFrequencyLimit) {
        this.alertFrequencyLimit = alertFrequencyLimit;
    }

    // 阈值配置内部类
    public static class Threshold implements Serializable {

        private static final long serialVersionUID = 1L;

        private int cpu = 80;
        private int memory = 80;
        private int disk = 90;
        private int jvmMemory = 80;
        private int threadCount = 500;
        private long responseTime = 5000; // 毫秒

        // getter和setter方法
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

        public int getJvmMemory() {
            return jvmMemory;
        }

        public void setJvmMemory(int jvmMemory) {
            this.jvmMemory = jvmMemory;
        }

        public int getThreadCount() {
            return threadCount;
        }

        public void setThreadCount(int threadCount) {
            this.threadCount = threadCount;
        }

        public long getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(long responseTime) {
            this.responseTime = responseTime;
        }
    }

    // 通知配置内部类
    public static class Notification implements Serializable {

        private static final long serialVersionUID = 1L;

        private boolean emailEnabled = false;
        private String emailRecipients = "admin@example.com";
        private boolean webhookEnabled = false;
        private String webhookUrl = "https://example.com/webhook";
        private boolean smsEnabled = false;
        private String smsRecipients = "";

        // getter和setter方法
        public boolean isEmailEnabled() {
            return emailEnabled;
        }

        public void setEmailEnabled(boolean emailEnabled) {
            this.emailEnabled = emailEnabled;
        }

        public String getEmailRecipients() {
            return emailRecipients;
        }

        public void setEmailRecipients(String emailRecipients) {
            this.emailRecipients = emailRecipients;
        }

        public boolean isWebhookEnabled() {
            return webhookEnabled;
        }

        public void setWebhookEnabled(boolean webhookEnabled) {
            this.webhookEnabled = webhookEnabled;
        }

        public String getWebhookUrl() {
            return webhookUrl;
        }

        public void setWebhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
        }

        public boolean isSmsEnabled() {
            return smsEnabled;
        }

        public void setSmsEnabled(boolean smsEnabled) {
            this.smsEnabled = smsEnabled;
        }

        public String getSmsRecipients() {
            return smsRecipients;
        }

        public void setSmsRecipients(String smsRecipients) {
            this.smsRecipients = smsRecipients;
        }
    }
}