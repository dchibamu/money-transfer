package org.chibamuio.moneytransfer.exceptions;

public class ErrorMessage {

    private int status;
    private int code;
    private String message;


    public ErrorMessage(){}

    public ErrorMessage(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public ErrorMessage(BusinessException bex) {
        status = bex.getStatus();
        code = bex.getCode();
        message = bex.getMessage();
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

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "status=" + status +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
