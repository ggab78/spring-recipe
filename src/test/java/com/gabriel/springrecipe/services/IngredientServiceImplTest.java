package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.converters.IngredientCommandToIngredient;
import com.gabriel.springrecipe.converters.IngredientToIngredientCommand;
import com.gabriel.springrecipe.domain.Ingredient;
import com.gabriel.springrecipe.domain.Recipe;
import com.gabriel.springrecipe.domain.UnitOfMeasure;
import com.gabriel.springrecipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class IngredientServiceImplTest {

    IngredientServiceImpl ingredientServiceImpl;

    @Mock
    RecipeService recipeService;
    @Mock
    IngredientToIngredientCommand ingredientToIngredientCommand;
    @Mock
    IngredientCommandToIngredient ingredientCommandToIngredient;
    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    //MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientServiceImpl = new IngredientServiceImpl(recipeService,
                ingredientToIngredientCommand, ingredientCommandToIngredient, unitOfMeasureReactiveRepository);
       // mockMvc = MockMvcBuilders.standaloneSetup(ingredientServiceImpl).build();
    }

    @Test
    public void findIngredientByRecipeIdAndId() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient = new Ingredient();
        ingredient.setId("2");
        recipe.addIngredient(ingredient);

        when(recipeService.getRecipeById(anyString())).thenReturn(Mono.just(recipe));

        //when
        Ingredient returnedIngredient
                = ingredientServiceImpl.findIngredientByRecipeIdAndId("1","2").block();

        //then
        assertNotNull(returnedIngredient);
        assertEquals("2", returnedIngredient.getId());

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


        when(recipeService.getRecipeById(anyString())).thenReturn(Mono.just(recipe));
        when(ingredientToIngredientCommand.convert(any())).thenReturn(ingredientCommand);

        IngredientCommand foundCommand = ingredientServiceImpl.findIngredientCommandByRecipeIdAndId("1","1").block();

        assertEquals(ingredientCommand.getId(), foundCommand.getId());
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
        unitOfMeasure.setId("1");

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setDescription("uom");
        unitOfMeasureCommand.setId("1");

        ingredientCommand.setUom(unitOfMeasureCommand);

        when(recipeService.getRecipeById(any())).thenReturn(Mono.just(recipe));
        when(recipeService.saveRecipe(any())).thenReturn(Mono.just(recipe));
        when(ingredientToIngredientCommand.convert(any())).thenReturn(ingredientCommand);
        when(unitOfMeasureReactiveRepository.findById(anyString())).thenReturn(Mono.just(unitOfMeasure));

        //when
        IngredientCommand foundIngredientCommand = ingredientServiceImpl.saveIngredientCommand(ingredientCommand).block();

        //then
        assertEquals("command", ingredient.getDescription());
        assertEquals(new BigDecimal(12.0), ingredient.getAmount());
        assertEquals("uom", ingredient.getUom().getDescription());
        assertEquals("1", foundIngredientCommand.getId());
        assertEquals("command", foundIngredientCommand.getDescription());
        verify(ingredientToIngredientCommand, times(1)).convert(any());
        verify(unitOfMeasureReactiveRepository, times(1)).findById(anyString());
    }

    @Test
    public void saveNewIngredientCommand() {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        //id is not available for the new command
        //ingredientCommand.setId(1L);
        ingredientCommand.setDescription("command");
        ingredientCommand.setAmount(new BigDecimal(12.0));
        ingredientCommand.setRecipeId("1");

        Ingredient ingredient = new Ingredient();
        ingredient.setDescription("command");
        ingredient.setAmount(new BigDecimal(12.0));

        Recipe recipe = new Recipe();
        recipe.setId("1");
        recipe.getIngredients().add(ingredient);

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setDescription("uom");
        unitOfMeasureCommand.setId("1");
        ingredientCommand.setUom(unitOfMeasureCommand);


        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription("uom");
        unitOfMeasure.setId("1");
        ingredient.setUom(unitOfMeasure);


        when(recipeService.getRecipeById(anyString())).thenReturn(Mono.just(new Recipe()));
        when(recipeService.saveRecipe(any())).thenReturn(Mono.just(recipe));
        when(ingredientCommandToIngredient.convert(any())).thenReturn(ingredient);
        when(ingredientToIngredientCommand.convert(any())).thenReturn(ingredientCommand);

        //when
        IngredientCommand foundIngredientCommand = ingredientServiceImpl.saveIngredientCommand(ingredientCommand).block();

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
        when(recipeService.getRecipeById(anyString())).thenReturn(Mono.just(recipe));
        ingredientServiceImpl.deleteIngredientByRecipeIdAndId("1", "1");

        //then
        verify(recipeService, times(1)).saveRecipe(any());


        //when
        when(recipeService.getRecipeById(anyString())).thenReturn(Mono.empty());
        //then
        try{
            ingredientServiceImpl.deleteIngredientByRecipeIdAndId("1", "1");
        }catch (Exception e){
            assertEquals("Recipe with id = 1 not found",e.getMessage());
            throw e;
        }

        //when
        when(recipeService.getRecipeById(anyString())).thenReturn(Mono.just(new Recipe()));
        //then
        try{
            ingredientServiceImpl.deleteIngredientByRecipeIdAndId("1", "1");
        }catch (Exception e){
            assertEquals("Recipe doesn't contain ingredients",e.getMessage());
            throw e;
        }
    }
}