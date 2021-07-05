package com.dulanja33.dcl.dao;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import com.dulanja33.dcl.properties.YamlMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class DBHandlerDaoImplTest {

    JdbcTemplate jdbcTemplate;

    @Test
    void testShouldUpdateData() throws DclException {
        //given
        YamlMapping yamlMapping = new YamlMapping();
        yamlMapping.setCheckQuery("select count(*) from TEST where DATA = ${data}");
        yamlMapping.setInsertQuery("insert into TEST(DATA,VALUE) values ${(${data},${data2})}");
        Context context = Context.builder()
                .yamlMapping(yamlMapping)
                .build();
        List<Map<String, String>> preProcessedRecords = new ArrayList<>();
        Map<String, String> mp = new HashMap<>();
        mp.put("data", "'a1'");
        mp.put("data2", "'a2'");
        preProcessedRecords.add(mp);
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:jdbc/schema.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);

        //when
        DBHandlerDao dbHandlerDao = new DBHandlerDaoImpl(jdbcTemplate);
        int i = dbHandlerDao.updateData(context, preProcessedRecords, 1);

        //then
        Assertions.assertEquals(1, i);


    }
}