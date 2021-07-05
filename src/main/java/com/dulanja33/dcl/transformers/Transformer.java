package com.dulanja33.dcl.transformers;

import com.dulanja33.dcl.exception.DclException;

public interface Transformer {
    String execute(String value) throws DclException;
}
