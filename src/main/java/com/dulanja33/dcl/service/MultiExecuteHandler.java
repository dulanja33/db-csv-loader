package com.dulanja33.dcl.service;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import org.apache.commons.csv.CSVRecord;

public interface MultiExecuteHandler {
    void executeDBInsert(Context context, Iterable<CSVRecord> csvRecords) throws DclException;
}
