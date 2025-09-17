package com.blog.monitorservice.exception;

/**
 * 监控服务基础异常类
 */
public class MonitorException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public MonitorException() {
        super();
    }
    
    public MonitorException(String message) {
        super(message);
    }
    
    public MonitorException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public MonitorException(Throwable cause) {
        super(cause);
    }
    
    protected MonitorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}