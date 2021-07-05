package com.dulanja33.dcl.service;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import com.dulanja33.dcl.properties.YamlMapping;

public interface YmlProcessor {
    YamlMapping readDataMappingYml(Context context) throws DclException;
}
