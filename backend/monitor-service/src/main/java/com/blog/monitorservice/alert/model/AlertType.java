package com.blog.monitorservice.alert.model;

public enum AlertType {

    /**
     * 系统资源告警，如CPU、内存、磁盘使用率过高
     */
    SYSTEM,

    /**
     * 服务告警，如服务不可用、响应时间过长等
     */
    SERVICE,

    /**
     * JVM告警，如JVM内存使用过高、线程数量过多等
     */
    JVM,

    /**
     * 应用程序告警，如应用异常、业务指标异常等
     */
    APPLICATION,

    /**
     * 数据库告警，如数据库连接异常、查询性能问题等
     */
    DATABASE,

    /**
     * 网络告警，如网络连接异常、网络延迟过高等
     */
    NETWORK,

    /**
     * 安全告警，如未授权访问、异常登录等
     */
    SECURITY,

    /**
     * 其他类型的告警
     */
    OTHER
}