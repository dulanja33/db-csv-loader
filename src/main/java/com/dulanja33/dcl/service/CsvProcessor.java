package com.dulanja33.dcl.service;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import org.apache.commons.csv.CSVRecord;

import java.util.Map;

public interface CsvProcessor {

    Iterable<CSVRecord> readDataCsvFile(Context context) throws DclException;

    Map<String, String> preProcessCsvRecord(Context context, CSVRecord csvRecord) throws DclException;
}
