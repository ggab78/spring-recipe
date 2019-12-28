package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.domain.Ingredient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class IngredientCommandToIngredientTest {

    IngredientCommandToIngredient ingredientCommandToIngredient;

    public static final String DESCRIPTION = "description";
    public static final String ID = "1";
    public static final BigDecimal AMOUNT = new BigDecimal(2.0);
    public static final String UOM_ID = "143";

    @Mock
    UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientCommandToIngredient = new IngredientCommandToIngredient(unitOfMeasureCommandToUnitOfMeasure);
    }

    @Test
    public void testNull(){
        assertNull(ingredientCommandToIngredient.convert(null));
    }

    @Test
    public void testEmpty(){
        assertNotNull(ingredientCommandToIngredient.convert(new IngredientCommand()));
    }

    @Test
    public void convert() {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(ID);
        ingredientCommand.setDescription(DESCRIPTION);
        ingredientCommand.setAmount(AMOUNT);

        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(UOM_ID);
        ingredientCommand.setUom(uomCommand);

        //when
        Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);

        //then
        assertNotNull(ingredient);
        assertEquals(ID, ingredient.getId());
        assertEquals(DESCRIPTION, ingredient.getDescription());
        assertEquals(AMOUNT, ingredient.getAmount());

        verify(unitOfMeasureCommandToUnitOfMeasure, times(1)).convert(any());

    }
}