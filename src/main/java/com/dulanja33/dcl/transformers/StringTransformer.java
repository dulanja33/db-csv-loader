package com.dulanja33.dcl.transformers;

public class StringTransformer implements Transformer {
    @Override
    public String execute(String value) {
        return "'" + value + "'";
    }
}
