package com.blog.monitorservice.alert.model;

public enum Severity {

    /**
     * 严重告警，需要立即处理
     */
    CRITICAL(1, "严重"),

    /**
     * 高优先级告警，需要尽快处理
     */
    HIGH(2, "高"),

    /**
     * 中优先级告警，需要在正常工作时间内处理
     */
    MEDIUM(3, "中"),

    /**
     * 低优先级告警，可以在适当的时候处理
     */
    LOW(4, "低"),

    /**
     * 信息性通知，不需要立即处理
     */
    INFO(5, "信息");

    private final int level;
    private final String description;

    Severity(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 判断当前告警是否比另一个告警更严重
     */
    public boolean isMoreSevereThan(Severity other) {
        return this.level < other.level;
    }

    /**
     * 判断当前告警是否比另一个告警轻
     */
    public boolean isLessSevereThan(Severity other) {
        return this.level > other.level;
    }

    /**
     * 根据级别获取Severity枚举
     */
    public static Severity fromLevel(int level) {
        for (Severity severity : values()) {
            if (severity.level == level) {
                return severity;
            }
        }
        throw new IllegalArgumentException("Invalid severity level: " + level);
    }

    /**
     * 根据描述获取Severity枚举
     */
    public static Severity fromDescription(String description) {
        for (Severity severity : values()) {
            if (severity.description.equalsIgnoreCase(description)) {
                return severity;
            }
        }
        throw new IllegalArgumentException("Invalid severity description: " + description);
    }
}