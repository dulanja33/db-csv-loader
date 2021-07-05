package com.dulanja33.dcl.service;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import com.dulanja33.dcl.properties.YamlMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DBCdvLoaderServiceImpl implements DBCsvLoaderService {

    private final CsvProcessor csvProcessor;
    private final YmlProcessor ymlProcessor;
    private final MultiExecuteHandler multiExecuteHandler;

    public DBCdvLoaderServiceImpl(CsvProcessor csvProcessor, YmlProcessor ymlProcessor, MultiExecuteHandler multiExecuteHandler) {
        this.csvProcessor = csvProcessor;
        this.ymlProcessor = ymlProcessor;
        this.multiExecuteHandler = multiExecuteHandler;
    }

    @Override
    public void loadData(Context context) {
        try {
            YamlMapping yamlMapping = ymlProcessor.readDataMappingYml(context);
            context.setYamlMapping(yamlMapping);
            Iterable<CSVRecord> csvRecords = csvProcessor.readDataCsvFile(context);
            multiExecuteHandler.executeDBInsert(context, csvRecords);
        } catch (DclException e) {
            log.error("Error updating data: {}", e.getMessage());
        }
    }

}
