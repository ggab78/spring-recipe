package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitOfMeasureCommandToUnitOfMeasureTest {

    UnitOfMeasureCommandToUnitOfMeasure converter;

    private static final Long ID = 1L;
    private static final String DESCRIPTION = "description";

    @Before
    public void setUp(){
        converter = new UnitOfMeasureCommandToUnitOfMeasure();
    }

    @Test
    public void testNull() throws Exception{
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmpty()throws Exception{
        assertNotNull(converter.convert(new UnitOfMeasureCommand()));
    }

    @Test
    public void convert() throws Exception {
        //given
        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(ID);
        uomCommand.setDescription(DESCRIPTION);

        //when
        UnitOfMeasure uom = converter.convert(uomCommand);

        //then
        assertNotNull(uom);
        assertEquals(DESCRIPTION, uom.getDescription());
        assertEquals(ID, uom.getId());
    }
}