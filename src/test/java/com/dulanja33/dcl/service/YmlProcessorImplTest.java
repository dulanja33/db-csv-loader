package com.dulanja33.dcl.service;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import com.dulanja33.dcl.properties.YamlMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

class YmlProcessorImplTest {

    @Test
    void testShouldReadDataMappingYml() throws DclException, URISyntaxException {

        //given
        URL mappingPath = getClass().getClassLoader().getResource("mapping.yml");
        File mapping = Paths.get(mappingPath.toURI()).toFile();
        String mappingAbsolutePath = mapping.getAbsolutePath();
        YmlProcessor ymlProcessor = new YmlProcessorImpl();
        Context context = Context.builder()
                .mappingPath(mappingAbsolutePath)
                .build();

        //when
        YamlMapping yamlMapping = ymlProcessor.readDataMappingYml(context);

        //then
        Assertions.assertEquals("data1", yamlMapping.getMapping().get(0).getCsvCol());
    }

    @Test
    void testShouldGiveExceptionWhenReadMappingYml() {
        //given
        Context context = Context.builder()
                .mappingPath("")
                .build();

        //when
        YmlProcessor ymlProcessor = new YmlProcessorImpl();

        //then
        Assertions.assertThrows(DclException.class, () -> {
            ymlProcessor.readDataMappingYml(context);
        });
    }
}