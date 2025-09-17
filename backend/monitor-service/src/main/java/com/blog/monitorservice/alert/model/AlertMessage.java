package com.blog.monitorservice.alert.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AlertMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String alertKey;
    private AlertType alertType;
    private String title;
    private String message;
    private Severity severity;
    private String serverIp;
    private String serviceName;
    private String instanceId;
    private long timestamp;
    private Map<String, Object> additionalInfo = new HashMap<>();

    // getter和setter方法
    public String getAlertKey() {
        return alertKey;
    }

    public void setAlertKey(String alertKey) {
        this.alertKey = alertKey;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void addAdditionalInfo(String key, Object value) {
        this.additionalInfo.put(key, value);
    }

    @Override
    public String toString() {
        return "AlertMessage{" +
                "alertKey='" + alertKey + '\'' +
                ", alertType=" + alertType +
                ", title='" + title + '\'' +
                ", severity=" + severity +
                ", timestamp=" + timestamp +
                '}';
    }
}