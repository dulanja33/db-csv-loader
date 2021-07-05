package com.dulanja33.dcl.dao;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;

import java.util.List;
import java.util.Map;

public interface DBHandlerDao {
    int updateData(Context context, List<Map<String, String>> preProcessedRecords, int batchNumber) throws DclException;
}
