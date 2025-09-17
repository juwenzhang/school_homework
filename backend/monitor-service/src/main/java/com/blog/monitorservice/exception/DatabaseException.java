package com.blog.monitorservice.exception;

/**
 * 数据库操作异常类
 */
public class DatabaseException extends MonitorException {
    
    private static final long serialVersionUID = 1L;
    
    public DatabaseException() {
        super();
    }
    
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DatabaseException(Throwable cause) {
        super(cause);
    }
    
    protected DatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}