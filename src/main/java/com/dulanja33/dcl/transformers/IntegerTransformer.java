package com.dulanja33.dcl.transformers;

import com.dulanja33.dcl.exception.DclException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IntegerTransformer implements Transformer {

    @Override
    public String execute(String value) throws DclException {
        try {
            return String.valueOf(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            log.error("unable to parse integer value: {}", e.getMessage());
            throw new DclException("unable to parse integer value");
        }
    }
}
