package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.converters.IngredientCommandToIngredient;
import com.gabriel.springrecipe.converters.IngredientToIngredientCommand;
import com.gabriel.springrecipe.converters.RecipeToRecipeCommand;
import com.gabriel.springrecipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.gabriel.springrecipe.domain.Ingredient;
import com.gabriel.springrecipe.domain.Recipe;
import com.gabriel.springrecipe.domain.UnitOfMeasure;
import com.gabriel.springrecipe.repositories.IngredientRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class IngredientServiceImplTest {

    IngredientServiceImpl ingredientServiceImpl;

    @Mock
    IngredientRepository ingredientRepository;
    @Mock
    RecipeService recipeService;
    @Mock
    IngredientToIngredientCommand ingredientToIngredientCommand;
    @Mock
    IngredientCommandToIngredient ingredientCommandToIngredient;
    @Mock
    UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientServiceImpl = new IngredientServiceImpl(ingredientRepository, recipeService,
                ingredientToIngredientCommand, ingredientCommandToIngredient, unitOfMeasureCommandToUnitOfMeasure);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientServiceImpl).build();
    }

    @Test (expected = RuntimeException.class)
    public void findIngredientByRecipeIdAndId() throws RuntimeException {
        //given
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient = new Ingredient();
        ingredient.setId("2");
        recipe.addIngredient(ingredient);

        Optional<Ingredient> ingredientOptional = Optional.of(ingredient);

        when(recipeService.getRecipeById(anyString())).thenReturn(recipe);
        //when(ingredientRepository.findByRecipeAndId(any(),anyString())).thenReturn(ingredientOptional);

        //when
        Ingredient returnedIngredient = ingredientServiceImpl.findIngredientByRecipeIdAndId("1","2");

        //then
        assertNotNull(returnedIngredient);
        assertEquals((Long)2L, returnedIngredient.getId());

        //when
        when(recipeService.getRecipeById(anyString())).thenReturn(new Recipe());
        try{
            ingredientServiceImpl.findIngredientByRecipeIdAndId("1","2");
        }catch(RuntimeException re){
            assertEquals("Recipe doesn't contain ingredients",re.getMessage());
            throw re;
        }
    }

    @Test
    public void findIngredientCommandByRecipeIdAndId() {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId("2");

        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient = new Ingredient();
        ingredient.setId("1");
        recipe.addIngredient(ingredient);

        Optional<Ingredient> ingredientOptional = Optional.of(ingredient);

        when(recipeService.getRecipeById(anyString())).thenReturn(recipe);
       // when(ingredientRepository.findByRecipeAndId(any(),anyString())).thenReturn(ingredientOptional);


        //when
        when(ingredientToIngredientCommand.convert(any())).thenReturn(ingredientCommand);

        ingredientServiceImpl.findIngredientCommandByRecipeIdAndId("1","1");

        verify(ingredientToIngredientCommand, times(1)).convert(any());

    }

    @Test
    public void saveExistingIngredientCommand() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient = new Ingredient();
        ingredient.setId("1");
        recipe.addIngredient(ingredient);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId("1");
        ingredientCommand.setDescription("command");
        ingredientCommand.setAmount(new BigDecimal(12.0));

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription("uom");

        when(recipeService.getRecipeById(any())).thenReturn(recipe);
        when(ingredientToIngredientCommand.convert(any())).thenReturn(ingredientCommand);
        when(unitOfMeasureCommandToUnitOfMeasure.convert(any())).thenReturn(unitOfMeasure);

        //when
        IngredientCommand foundIngredientCommand = ingredientServiceImpl.saveIngredientCommand(ingredientCommand);

        //then
        assertEquals("command", ingredient.getDescription());
        assertEquals(new BigDecimal(12.0), ingredient.getAmount());
        assertEquals("uom", ingredient.getUom().getDescription());
        assertEquals((Long)1L, foundIngredientCommand.getId());
        assertEquals("command", foundIngredientCommand.getDescription());
        verify(unitOfMeasureCommandToUnitOfMeasure, times(1)).convert(any());
    }

    @Test
    public void saveNewIngredientCommand() {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        //id is not available for the new command
        //ingredientCommand.setId(1L);
        ingredientCommand.setDescription("command");
        ingredientCommand.setAmount(new BigDecimal(12.0));

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setDescription("uom");
        ingredientCommand.setUom(unitOfMeasureCommand);

        when(recipeService.getRecipeById(any())).thenReturn(new Recipe());
        when(ingredientCommandToIngredient.convert(any())).thenReturn(new Ingredient());
        when(ingredientToIngredientCommand.convert(any())).thenReturn(ingredientCommand);

        //when
        IngredientCommand foundIngredientCommand = ingredientServiceImpl.saveIngredientCommand(ingredientCommand);

        //then
        assertEquals("command", foundIngredientCommand.getDescription());
        assertEquals(new BigDecimal(12.0), foundIngredientCommand.getAmount());
        assertEquals("uom", foundIngredientCommand.getUom().getDescription());

        verify(ingredientCommandToIngredient, times(1)).convert(any());
    }

    @Test (expected = Exception.class)
    public void deleteIngredientByRecipeIdAndId() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient = new Ingredient();
        ingredient.setId("1");
        recipe.addIngredient(ingredient);

        //when
        when(recipeService.getRecipeById(anyString())).thenReturn(recipe);
        ingredientServiceImpl.deleteIngredientByRecipeIdAndId("1", "1");

        //then
        verify(ingredientRepository, times(1)).delete(any());
        verify(recipeService, times(1)).saveRecipe(any());


        //when
        when(recipeService.getRecipeById(anyString())).thenReturn(null);
        //then
        try{
            ingredientServiceImpl.deleteIngredientByRecipeIdAndId("1", "1");
        }catch (Exception e){
            assertEquals("Recipe with id = 1 not found",e.getMessage());
            throw e;
        }

        //when
        when(recipeService.getRecipeById(anyString())).thenReturn(new Recipe());
        //then
        try{
            ingredientServiceImpl.deleteIngredientByRecipeIdAndId("1", "1");
        }catch (Exception e){
            assertEquals("Recipe doesn't contain ingredients",e.getMessage());
            throw e;
        }


    }
}