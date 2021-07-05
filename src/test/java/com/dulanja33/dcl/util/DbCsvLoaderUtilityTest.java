package com.dulanja33.dcl.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DbCsvLoaderUtilityTest {

    @Test
    void substituteValues() {
        Map<String, String> mp = new HashMap<>();
        mp.put("name value", "John");
        mp.put("country_value", "Singapore");
        String actual = DbCsvLoaderUtility.substituteValues("this is ${name value} and I am from ${country_value}", mp);
        Assertions.assertEquals("this is John and I am from Singapore", actual);
    }

    @Test
    void substituteArray() {
        List<Map<String, String>> data = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            Map<String, String> mp = new HashMap<>();
            mp.put("data", "a" + i);
            mp.put("value", "b" + i);
            mp.put("date sfsdf", "c" + i);
            data.add(mp);
        }
        String actual = DbCsvLoaderUtility.substituteArray("insert into TEST(DATA,value,date) values ${(${data},${value},${date sfsdf})} ", data);
        Assertions.assertEquals("insert into TEST(DATA,value,date) values (a1,b1,c1),(a2,b2,c2) ", actual);
    }

    @Test
    void escapeString() {
        String value = "#10-4 1/8\" x, 9 1/2\" Premium Diagonal Seam Envelopes";
        String expected = "#10-4 1/8\\\\\" x, 9 1/2\\\\\" Premium Diagonal Seam Envelopes";
        String actual = DbCsvLoaderUtility.escapeString(value, '\\', '\"');
        Assertions.assertEquals(expected,actual);
    }
}