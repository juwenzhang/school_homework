package com.blog.monitorservice.exception;

/**
 * 通知服务异常类
 */
public class NotificationException extends MonitorException {
    
    private static final long serialVersionUID = 1L;
    
    public NotificationException() {
        super();
    }
    
    public NotificationException(String message) {
        super(message);
    }
    
    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NotificationException(Throwable cause) {
        super(cause);
    }
    
    protected NotificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}