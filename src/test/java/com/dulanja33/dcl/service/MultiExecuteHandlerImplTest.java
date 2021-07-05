package com.dulanja33.dcl.service;

import com.dulanja33.dcl.dao.DBHandlerDao;
import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import com.dulanja33.dcl.properties.YamlMapping;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MultiExecuteHandlerImplTest {

    MultiExecuteHandler multiExecuteHandler;

    @Mock
    private CsvProcessor csvProcessor;

    @Mock
    private DBHandlerDao dbHandlerDao;

    @Test
    void testShouldExecuteDBInsert() throws DclException, URISyntaxException {
        //given
        multiExecuteHandler = new MultiExecuteHandlerImpl(csvProcessor, dbHandlerDao);
        Context context = Context.builder().chunkSize(5).build();
        Map<String, String> mp = new HashMap<>();
        mp.put("data1", "1");
        mp.put("data2", "2");
        when(csvProcessor.preProcessCsvRecord(Mockito.any(Context.class), Mockito.any(CSVRecord.class)))
                .thenReturn(mp);

        when(dbHandlerDao.updateData(Mockito.any(Context.class), Mockito.anyList(), Mockito.anyInt())).thenReturn(1);
        URL csvPath = getClass().getClassLoader().getResource("test.csv");
        File csv = Paths.get(csvPath.toURI()).toFile();
        String csvAbsolutePath = csv.getAbsolutePath();
        URL mappingPath = getClass().getClassLoader().getResource("mapping.yml");
        File mapping = Paths.get(mappingPath.toURI()).toFile();
        String mappingAbsolutePath = mapping.getAbsolutePath();
        context.setMappingPath(mappingAbsolutePath);
        context.setCsvPath(csvAbsolutePath);

        YmlProcessor ymlProcessor = new YmlProcessorImpl();
        YamlMapping yamlMapping = ymlProcessor.readDataMappingYml(context);
        context.setYamlMapping(yamlMapping);

        CsvProcessorImpl csvProcessorImpl = new CsvProcessorImpl();
        Iterable<CSVRecord> csvRecords = csvProcessorImpl.readDataCsvFile(context);


        //when
        multiExecuteHandler.executeDBInsert(context, csvRecords);

        //then
        verify(dbHandlerDao).updateData(Mockito.any(Context.class), Mockito.anyList(), Mockito.anyInt());
    }
}