package com.dulanja33.dcl.service;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import com.dulanja33.dcl.properties.YamlMapping;
import com.dulanja33.dcl.transformers.Transformer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CsvProcessorImpl implements CsvProcessor {

    @Override
    public Iterable<CSVRecord> readDataCsvFile(Context context) throws DclException {
        try {
            Reader csv = new FileReader(context.getCsvPath());
            String[] csvHeaders = context.getYamlMapping().getMapping().stream()
                    .map(data -> data.getCsvCol().trim())
                    .toArray(String[]::new);
            return CSVFormat.DEFAULT
                    .withHeader(csvHeaders)
                    .withTrim()
                    .withFirstRecordAsHeader()
                    .parse(csv);

        } catch (IOException e) {
            log.error("Error reading csv file: {}", e.getMessage());
            throw new DclException("Error reading csv file");
        }
    }

    @Override
    public Map<String, String> preProcessCsvRecord(Context context, CSVRecord csvRecord) throws DclException {
        List<YamlMapping.MapData> mapDataList = context.getYamlMapping().getMapping();
        List<String> headers = mapDataList.stream()
                .map(YamlMapping.MapData::getCsvCol)
                .collect(Collectors.toList());

        Map<String, String> processedCsvMap = new HashMap<>();

        for (String header : headers) {
            String value = csvRecord.get(header);
            YamlMapping.MapData mapData = mapDataList.stream()
                    .filter(data -> data.getCsvCol().trim().equals(header))
                    .findFirst()
                    .orElseThrow(() -> new DclException(String.format("no map data found for header: %s", header)));
            try {
                Transformer transformer = (Transformer) Class.forName(mapData.getTransformer()).newInstance();
                String transformedVal = transformer.execute(value);
                processedCsvMap.put(mapData.getCsvCol().trim(), transformedVal);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                log.error("Unable create instance transformer class: {}", mapData.getTransformer());
                throw new DclException("Unable to pre process csv record");
            }
        }

        return processedCsvMap;

    }
}
