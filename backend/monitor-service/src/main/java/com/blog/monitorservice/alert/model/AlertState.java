package com.blog.monitorservice.alert.model;

public enum AlertState {

    /**
     * 告警处于活跃状态
     */
    ACTIVE,

    /**
     * 告警已解决
     */
    RESOLVED,

    /**
     * 告警已确认，但尚未解决
     */
    ACKNOWLEDGED,

    /**
     * 告警已抑制，暂时不会通知
     */
    SUPPRESSED,

    /**
     * 告警已关闭，不再需要关注
     */
    CLOSED,

    /**
     * 告警已过期
     */
    EXPIRED
}