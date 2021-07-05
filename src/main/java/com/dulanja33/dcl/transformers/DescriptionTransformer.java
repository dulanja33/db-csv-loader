package com.dulanja33.dcl.transformers;


import com.dulanja33.dcl.util.DbCsvLoaderUtility;

public class DescriptionTransformer implements Transformer {

    @Override
    public String execute(String value) {
        return "'" + DbCsvLoaderUtility.escapeString(value,'\\','\'') + "'";
    }
}
