package com.dulanja33.dcl.util;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CliUtilTest {

    @Test
    void testShouldCliOptionHandle() throws DclException {
        //given
        String args = "--mapping,mapping_sales.yml,--csv,sales.csv,--chunkSize,3";

        //when
        Context context = CliUtil.cliOptionHandle(args.split(","));

        //then
        Assertions.assertEquals("mapping_sales.yml", context.getMappingPath());
        Assertions.assertEquals("sales.csv", context.getCsvPath());
        Assertions.assertEquals(3, context.getChunkSize());
    }
}