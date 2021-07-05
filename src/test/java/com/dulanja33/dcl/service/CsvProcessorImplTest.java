package com.dulanja33.dcl.service;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import com.dulanja33.dcl.properties.YamlMapping;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class CsvProcessorImplTest {

    Context context;
    CsvProcessor csvProcessor;

    @BeforeEach
    void setUp() throws URISyntaxException, DclException {
        URL csvPath = getClass().getClassLoader().getResource("test.csv");
        URL mappingPath = getClass().getClassLoader().getResource("mapping.yml");
        File csv = Paths.get(csvPath.toURI()).toFile();
        File mapping = Paths.get(mappingPath.toURI()).toFile();
        String csvAbsolutePath = csv.getAbsolutePath();
        String mappingAbsolutePath = mapping.getAbsolutePath();

        YmlProcessor ymlProcessor = new YmlProcessorImpl();


        context = Context.builder()
                .csvPath(csvAbsolutePath)
                .mappingPath(mappingAbsolutePath)
                .build();

        YamlMapping yamlMapping = ymlProcessor.readDataMappingYml(context);
        context.setYamlMapping(yamlMapping);

        csvProcessor = new CsvProcessorImpl();
    }

    @Test
    void testShouldReadDataCsvFile() throws DclException {
        //given
        //when
        //then
        Iterable<CSVRecord> csvRecords = csvProcessor.readDataCsvFile(context);
        int count = 1;
        for (CSVRecord csvRecord : csvRecords) {
            Map<String, String> record = csvProcessor.preProcessCsvRecord(context, csvRecord);
            Assertions.assertEquals("'" + count + "'", record.get("data1"));
            Assertions.assertEquals((count * 2), Integer.valueOf(record.get("data2")));
            count++;
        }


    }

    @Test
    void testShouldPreProcessCsvRecord() throws DclException, URISyntaxException {
        //given
        //when
        //then
        AtomicInteger count = new AtomicInteger(1);
        csvProcessor.readDataCsvFile(context).forEach(v -> {
            Assertions.assertEquals(count.get(), Integer.parseInt(v.get("data1")));
            count.getAndIncrement();
        });
    }

}