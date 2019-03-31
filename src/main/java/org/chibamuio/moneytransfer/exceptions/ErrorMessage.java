package org.chibamuio.moneytransfer.exceptions;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {

    private int status;
    private int code;
    private String message;
    private String stackTrace;

    public ErrorMessage(){}

    public ErrorMessage(int status, int code, String message, String stackTrace) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.stackTrace = stackTrace;
    }

    public ErrorMessage(BusinessException bex) {
        status = bex.getStatus();
        code = bex.getCode();
        message = bex.getMessage();
        stackTrace = bex.getExceptionStackTrace();
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

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "status=" + status +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }
}
