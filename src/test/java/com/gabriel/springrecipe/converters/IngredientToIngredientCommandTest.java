package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.domain.Ingredient;
import com.gabriel.springrecipe.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IngredientToIngredientCommandTest {


    public static final Long ID =1L;
    public static final String DESCRIPTION = "description";
    public static final BigDecimal AMOUNT = new BigDecimal(2.0);
    public static final Long UOM_ID = 1L;
    public static final String UOM_DESCRIPTION = "uom_description";

    IngredientToIngredientCommand ingredientToIngredientCommand;

    @Mock
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientToIngredientCommand = new IngredientToIngredientCommand(unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    public void testNull() throws Exception {
    assertNull(ingredientToIngredientCommand.convert(null));
    }

    @Test
    public void testEmpty() throws Exception {
        assertNotNull(ingredientToIngredientCommand.convert(new Ingredient()));
    }

    @Test
    public void convert() throws Exception {
        //given
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ID);
        ingredient.setDescription(DESCRIPTION);
        ingredient.setAmount(AMOUNT);

        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(UOM_ID);
        uom.setDescription(UOM_DESCRIPTION);
        ingredient.setUom(uom);

        //When
        IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(ingredient);

        //then
        assertNotNull(ingredientCommand);
        assertEquals(ID, ingredientCommand.getId());
        assertEquals(DESCRIPTION, ingredientCommand.getDescription());
        assertEquals(AMOUNT, ingredientCommand.getAmount());
        verify(unitOfMeasureToUnitOfMeasureCommand, times(1)).convert(any());

    }
}