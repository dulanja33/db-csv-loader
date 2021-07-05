package com.dulanja33.dcl.dao;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dulanja33.dcl.util.DbCsvLoaderUtility.substituteArray;
import static com.dulanja33.dcl.util.DbCsvLoaderUtility.substituteValues;

@Slf4j
@Service
public class DBHandlerDaoImpl implements DBHandlerDao {

    final JdbcTemplate jdbcTemplate;

    public DBHandlerDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(rollbackFor = DclException.class)
    public int updateData(Context context, List<Map<String, String>> preProcessedRecords, int batchNumber) throws DclException {
        try {
            log.info("Start update data for batch number: {}", batchNumber);
            List<Map<String, String>> uniqueRecords = preProcessedRecords.stream().filter(record -> {
                String checkQuery = substituteValues(context.getYamlMapping().getCheckQuery(), record);
                Integer count = jdbcTemplate.queryForObject(checkQuery, Integer.class);
                return count != null && count == 0;
            }).collect(Collectors.toList());
            if (uniqueRecords.size() == 0) {
                log.info("Data already available for batchNumber: {}", batchNumber);
                return batchNumber;
            }
            String updateQuery = substituteArray(context.getYamlMapping().getInsertQuery(), uniqueRecords);
            jdbcTemplate.update(updateQuery);
            log.info("Completed update data for batch number: {}", batchNumber);
            return batchNumber;
        } catch (DataAccessException e) {
            log.error("error updating the data chunk: {}", e.getMessage());
            throw new DclException(String.format("error updating the data chunk number: %d", batchNumber));
        }
    }
}
