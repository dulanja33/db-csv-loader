package com.dulanja33.dcl.service;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.dao.DBHandlerDao;
import com.dulanja33.dcl.properties.Context;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class MultiExecuteHandlerImpl implements MultiExecuteHandler {

    private final CsvProcessor csvProcessor;
    private final DBHandlerDao dbHandlerDao;

    public MultiExecuteHandlerImpl(CsvProcessor csvProcessor, DBHandlerDao dbHandlerDao) {
        this.csvProcessor = csvProcessor;
        this.dbHandlerDao = dbHandlerDao;
    }

    @Override
    public void executeDBInsert(Context context, Iterable<CSVRecord> csvRecords) throws DclException {
        ExecutorService executorService = Executors.newWorkStealingPool();

        List<Callable<Integer>> callableList = new ArrayList<>();
        LinkedList<Map<String, String>> recordChunk = new LinkedList<>();

        int chunkSize = context.getChunkSize();
        int recordCount = 0;
        AtomicInteger atomicBatchNumber = new AtomicInteger(-1);
        Set<Integer> successBatchNumbers = new HashSet<>();

        for (CSVRecord record : csvRecords) {
            recordCount++;
            Map<String, String> preProcessCsvRecord = csvProcessor.preProcessCsvRecord(context, record);
            recordChunk.add(preProcessCsvRecord);
            if (recordCount % chunkSize == 0) {
                final int batchNumber = atomicBatchNumber.incrementAndGet();
                final List<Map<String, String>> list = new LinkedList<>(recordChunk);
                callableList.add(() -> dbHandlerDao.updateData(context, list, batchNumber));
                recordChunk.clear();
            }
        }

        //remaining list
        if (!recordChunk.isEmpty()) {
            final int batchNumber = atomicBatchNumber.incrementAndGet();
            final List<Map<String, String>> list = new LinkedList<>(recordChunk);
            callableList.add(() -> dbHandlerDao.updateData(context, list, batchNumber));
        }

        try {
            List<Future<Integer>> futures = executorService.invokeAll(callableList);
            for (Future<Integer> future : futures) {
                try {
                    Integer success = future.get();
                    successBatchNumbers.add(success);
                } catch (ExecutionException e) {
                    log.error("execution fail for one batch: {}", e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            log.error("batch update fails: {}", e.getMessage());
            throw new DclException("batch update fails");
        }

        successBatchNumbers.forEach(number -> log.info("Successfully inserted batch number: {}", number));
        executorService.shutdown();
    }
}
