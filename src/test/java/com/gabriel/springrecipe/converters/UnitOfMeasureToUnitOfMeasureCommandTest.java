package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitOfMeasureToUnitOfMeasureCommandTest {

    UnitOfMeasureToUnitOfMeasureCommand converter;

    public static final String DESCRIPTION = "description";
    public static final String ID = "1";

    @Before
    public void setUp() throws Exception {
        converter = new UnitOfMeasureToUnitOfMeasureCommand();
    }


    @Test
    public void testNull(){
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmpty(){
        assertNotNull(converter.convert(new UnitOfMeasure()));
    }


    @Test
    public void convert() {
        //given
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(ID);
        uom.setDescription(DESCRIPTION);

        //when
        UnitOfMeasureCommand uomCommand = converter.convert(uom);

        //then
        assertNotNull(uomCommand);
        assertEquals(ID,uomCommand.getId());
        assertEquals(DESCRIPTION,uomCommand.getDescription());
    }
}