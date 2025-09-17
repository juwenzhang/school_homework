package com.blog.monitorservice.alert;

import com.blog.monitorservice.alert.model.AlertConfig;
import com.blog.monitorservice.alert.model.AlertMessage;
import com.blog.monitorservice.alert.model.AlertStatus;
import com.blog.monitorservice.collector.model.JvmMetrics;
import com.blog.monitorservice.collector.model.ServiceMetrics;
import com.blog.monitorservice.collector.model.SystemMetrics;

public interface AlertService {

    /**
     * 检查系统指标是否触发告警
     */
    void checkSystemAlert(SystemMetrics metrics);

    /**
     * 检查服务指标是否触发告警
     */
    void checkServiceAlert(ServiceMetrics metrics);

    /**
     * 检查JVM指标是否触发告警
     */
    void checkJvmAlert(JvmMetrics metrics);

    /**
     * 发送告警通知
     */
    void sendAlert(AlertMessage alertMessage);

    /**
     * 检查是否需要发送告警
     */
    boolean shouldSendAlert(String alertKey);

    /**
     * 获取当前告警配置
     */
    AlertConfig getAlertConfig();

    /**
     * 更新告警配置
     */
    void updateAlertConfig(AlertConfig alertConfig);

    /**
     * 启用或禁用告警功能
     */
    void enableAlert(boolean enable);

    /**
     * 获取告警状态
     */
    AlertStatus getAlertStatus(String alertKey);
}