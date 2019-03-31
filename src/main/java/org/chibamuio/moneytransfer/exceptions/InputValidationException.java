package org.chibamuio.moneytransfer.exceptions;

import java.util.Map;

public class InputValidationException extends BusinessException{
    private Map<String, String> errors;

    public InputValidationException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    @Override
    public String getMessage(){
        return String.format("Invalid request parameters");
    }
}
