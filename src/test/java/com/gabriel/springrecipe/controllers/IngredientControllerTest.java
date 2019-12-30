package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.services.IngredientService;
import com.gabriel.springrecipe.services.RecipeService;
import com.gabriel.springrecipe.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {


    IngredientController ingredientController;

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    Model model;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientController = new IngredientController(recipeService, ingredientService, unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    public void listIngredients() throws Exception {
        //given
        String recipeId = "1";
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipeId);

        //when
        when(recipeService.getRecipeCommandById(anyString())).thenReturn(Mono.just(recipeCommand));

        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/ingredients/list"))
                .andExpect(model().attributeExists("recipe"));
        //then
        verify(recipeService, times(1)).getRecipeCommandById(anyString());


        //when
        ingredientController.listIngredients(model, "1");
        //then
        verify(model, times(1)).addAttribute(eq("recipe"), any());

    }

    @Test
    public void showIngredient() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();

        //when
        when(ingredientService.findIngredientCommandByRecipeIdAndId(anyString(), anyString())).thenReturn(Mono.just(ingredientCommand));

        //then
        mockMvc.perform(get("/recipe/1/ingredients/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/ingredients/show"))
                .andExpect(model().attributeExists("ingredient"));

    }

    @Test
    public void updateIngredient() throws Exception {

        when(ingredientService.findIngredientCommandByRecipeIdAndId(anyString(), anyString()))
                .thenReturn(Mono.just(new IngredientCommand()));
        when(unitOfMeasureService.findAllUomCommand()).thenReturn(Flux.just(new UnitOfMeasureCommand()));

        mockMvc.perform(get("/recipe/1/ingredients/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/ingredients/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));


        verify(ingredientService, times(1))
                .findIngredientCommandByRecipeIdAndId(anyString(), anyString());
        verify(unitOfMeasureService, times(1)).findAllUomCommand();

    }

    @Test
    public void saveOrUpdate() throws Exception {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId("2");
        command.setRecipeId("1");

        //when
        when(ingredientService.saveIngredientCommand(any())).thenReturn(Mono.just(command));

        //then
        mockMvc.perform(post("/recipe/1/postingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "")
                .param("amount", "")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients/2/show"));

    }

    @Test
    public void newIngredient() throws Exception {

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setDescription("uom");

        when(recipeService.getRecipeCommandById(anyString())).thenReturn(Mono.just(new RecipeCommand()));
        when(unitOfMeasureService.findAllUomCommand()).thenReturn(Flux.just(unitOfMeasureCommand));

        mockMvc.perform(get("/recipe/1/ingredients/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/ingredients/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

    }

    @Test
    public void deleteIngredient() throws Exception {

        ingredientController.deleteIngredient("1","1");

        verify(ingredientService, times(1)).deleteIngredientByRecipeIdAndId(anyString(), anyString());

        mockMvc.perform(get("/recipe/1/ingredients/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients"));
    }
}