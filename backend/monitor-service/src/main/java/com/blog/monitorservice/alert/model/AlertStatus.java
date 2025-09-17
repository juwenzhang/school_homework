package com.blog.monitorservice.alert.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class AlertStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private String alertKey;
    private long lastAlertTime;
    private AtomicInteger alertCount = new AtomicInteger(0);
    private AlertState state = AlertState.ACTIVE;
    private long firstAlertTime;
    private long lastRecoveryTime;
    private int recoveryCount = 0;

    // 构造方法
    public AlertStatus() {
        this.firstAlertTime = System.currentTimeMillis();
    }

    // getter和setter方法
    public String getAlertKey() {
        return alertKey;
    }

    public void setAlertKey(String alertKey) {
        this.alertKey = alertKey;
    }

    public long getLastAlertTime() {
        return lastAlertTime;
    }

    public void setLastAlertTime(long lastAlertTime) {
        this.lastAlertTime = lastAlertTime;
    }

    public int getAlertCount() {
        return alertCount.get();
    }

    public void setAlertCount(int alertCount) {
        this.alertCount.set(alertCount);
    }

    public void incrementAlertCount() {
        this.alertCount.incrementAndGet();
    }

    public AlertState getState() {
        return state;
    }

    public void setState(AlertState state) {
        this.state = state;
    }

    public long getFirstAlertTime() {
        return firstAlertTime;
    }

    public void setFirstAlertTime(long firstAlertTime) {
        this.firstAlertTime = firstAlertTime;
    }

    public long getLastRecoveryTime() {
        return lastRecoveryTime;
    }

    public void setLastRecoveryTime(long lastRecoveryTime) {
        this.lastRecoveryTime = lastRecoveryTime;
    }

    public int getRecoveryCount() {
        return recoveryCount;
    }

    public void setRecoveryCount(int recoveryCount) {
        this.recoveryCount = recoveryCount;
    }

    public void incrementRecoveryCount() {
        this.recoveryCount++;
    }

    /**
     * 标记告警为已解决
     */
    public void markAsResolved() {
        this.state = AlertState.RESOLVED;
        this.lastRecoveryTime = System.currentTimeMillis();
        this.recoveryCount++;
    }

    /**
     * 标记告警为活跃
     */
    public void markAsActive() {
        this.state = AlertState.ACTIVE;
        this.lastAlertTime = System.currentTimeMillis();
        this.alertCount.incrementAndGet();
    }

    /**
     * 重置告警状态
     */
    public void reset() {
        this.alertCount.set(0);
        this.state = AlertState.ACTIVE;
        this.firstAlertTime = System.currentTimeMillis();
        this.lastAlertTime = 0;
        this.lastRecoveryTime = 0;
        this.recoveryCount = 0;
    }

    /**
     * 获取告警持续时间
     */
    public long getDuration() {
        if (state == AlertState.RESOLVED && lastRecoveryTime > 0) {
            return lastRecoveryTime - firstAlertTime;
        } else {
            return System.currentTimeMillis() - firstAlertTime;
        }
    }

    @Override
    public String toString() {
        return "AlertStatus{" +
                "alertKey='" + alertKey + '\'' +
                ", state=" + state +
                ", alertCount=" + alertCount +
                ", firstAlertTime=" + firstAlertTime +
                ", lastAlertTime=" + lastAlertTime +
                ", duration=" + getDuration() + "ms" +
                '}';
    }
}