package org.chibamuio.moneytransfer.exceptions;

public class BusinessException extends Exception {

    private int status;
    private int code;
    private String message;
    private String stackExceptionTrace;

    public BusinessException(int status, int code, String message, String stackExceptionTrace){
        super(message);
        this.status = status;
        this.code = code;
        this.stackExceptionTrace = stackExceptionTrace;
    }

    public BusinessException() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExceptionStackTrace() {
        return stackExceptionTrace;
    }

    public void setExceptionStackTrace(String stackExceptionTrace) {
        this.stackExceptionTrace = stackExceptionTrace;
    }
}

