package org.chibamuio.moneytransfer.util;

import org.chibamuio.moneytransfer.exceptions.BusinessException;
import org.chibamuio.moneytransfer.exceptions.InputValidationException;
import org.chibamuio.moneytransfer.rest.dto.BaseDTO;

import javax.validation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ValidationUtility {

    private static Validator createValidator() {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        Validator validator = factory.getValidator();
        factory.close();
        return validator;
    }

    public static void validateRequest(BaseDTO<? extends BaseDTO> dto) throws BusinessException {
        Map<String, String> errors = new HashMap<>();
        Validator validator = createValidator();
        Set<ConstraintViolation<BaseDTO>> violations = validator.validate(dto);
        if(!violations.isEmpty()){
            violations.stream().forEach( violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
            throw new InputValidationException(errors);
        }
    }
}
