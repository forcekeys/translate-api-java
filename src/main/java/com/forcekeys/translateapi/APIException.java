package com.forcekeys.translateapi;

/**
 * API Error Exception
 */
public class APIException extends Exception {
    private final String errorCode;
    private final int statusCode;
    private final Integer retryAfter;
    
    public APIException(String message) {
        this(message, null, 0, null);
    }
    
    public APIException(String message, String errorCode, int statusCode, Integer retryAfter) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.retryAfter = retryAfter;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public Integer getRetryAfter() {
        return retryAfter;
    }
}