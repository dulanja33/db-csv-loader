package com.dulanja33.dcl.service;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import com.dulanja33.dcl.properties.YamlMapping;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DBCdvLoaderServiceImplTest {

    DBCsvLoaderService dbCdvLoaderService;

    @Mock
    private CsvProcessor csvProcessor;

    @Mock
    private YmlProcessor ymlProcessor;

    @Mock
    private MultiExecuteHandler multiExecuteHandler;

    @Test
    void testShouldLoadData() throws DclException {
        //given
        Context context = Context.builder().build();
        DBCsvLoaderService dbCdvLoaderService = new DBCdvLoaderServiceImpl(csvProcessor, ymlProcessor, multiExecuteHandler);
        YamlMapping yamlMapping = new YamlMapping();
        when(ymlProcessor.readDataMappingYml(Mockito.any(Context.class))).thenReturn(yamlMapping);
        Iterable<CSVRecord> csvRecords = new ArrayList<>();
        when(csvProcessor.readDataCsvFile(Mockito.any(Context.class))).thenReturn(csvRecords);
        doNothing().when(multiExecuteHandler).executeDBInsert(Mockito.any(Context.class), Mockito.anyList());
        //when

        dbCdvLoaderService.loadData(context);

        //then
        Assertions.assertEquals(yamlMapping,context.getYamlMapping());
        verify(multiExecuteHandler).executeDBInsert(context,csvRecords);
    }
}