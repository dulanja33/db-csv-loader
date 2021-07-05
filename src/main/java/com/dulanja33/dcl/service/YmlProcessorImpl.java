package com.dulanja33.dcl.service;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.util.DbCsvLoaderUtility;
import com.dulanja33.dcl.properties.Context;
import com.dulanja33.dcl.properties.YamlMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class YmlProcessorImpl implements YmlProcessor {

    public YamlMapping readDataMappingYml(Context context) throws DclException {
        String mappingPath = context.getMappingPath();
        return DbCsvLoaderUtility.readYmlFileToObject(mappingPath, YamlMapping.class);
    }
}
