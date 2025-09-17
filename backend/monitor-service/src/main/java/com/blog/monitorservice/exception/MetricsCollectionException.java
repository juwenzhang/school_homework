package com.blog.monitorservice.exception;

/**
 * 指标收集异常类
 */
public class MetricsCollectionException extends MonitorException {
    
    private static final long serialVersionUID = 1L;
    
    public MetricsCollectionException() {
        super();
    }
    
    public MetricsCollectionException(String message) {
        super(message);
    }
    
    public MetricsCollectionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public MetricsCollectionException(Throwable cause) {
        super(cause);
    }
    
    protected MetricsCollectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}