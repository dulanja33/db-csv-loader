package com.dulanja33.dcl.transformers;

import com.dulanja33.dcl.exception.DclException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TransformerTest {

    @Test
    void testShouldExecuteTransformerClasses() throws DclException {
        //given
        Transformer stringTransformer = new StringTransformer();
        Transformer integerTransformer = new IntegerTransformer();
        Transformer descriptionTransformer = new DescriptionTransformer();

        //when
        String stringVal = stringTransformer.execute("val");
        String intVal = integerTransformer.execute("5");
        String descVal = descriptionTransformer.execute("val");

        //then
        Assertions.assertEquals("'val'", stringVal);
        Assertions.assertEquals(5, Integer.valueOf(intVal));
        Assertions.assertEquals("'val'", descVal);
        Assertions.assertThrows(DclException.class, ()-> {
            integerTransformer.execute("s");
        });
    }
}