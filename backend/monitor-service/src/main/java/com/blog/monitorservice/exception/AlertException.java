package com.blog.monitorservice.exception;

/**
 * 告警服务异常类
 */
public class AlertException extends MonitorException {
    
    private static final long serialVersionUID = 1L;
    
    public AlertException() {
        super();
    }
    
    public AlertException(String message) {
        super(message);
    }
    
    public AlertException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AlertException(Throwable cause) {
        super(cause);
    }
    
    protected AlertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}