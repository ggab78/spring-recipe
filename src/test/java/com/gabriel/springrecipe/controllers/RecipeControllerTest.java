package com.gabriel.springrecipe.controllers;


import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.domain.Recipe;
import com.gabriel.springrecipe.exceptions.NotFoundException;
import com.gabriel.springrecipe.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    RecipeController recipeController;

    @Mock
    RecipeService recipeService;

    MockMvc mockMvc;

    @Mock
    Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        recipeController = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void getRecipeById() throws Exception {

        String recipeId = "1";
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);

        when(recipeService.getRecipeById(anyString())).thenReturn(recipe);
        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/show"))
                .andExpect(model().attributeExists("recipe"));


        String str = recipeController.getRecipeById(model, "1");
        assertEquals("recipes/show", str);

        verify(recipeService, times(2)).getRecipeById(anyString());
        verify(model, times(1)).addAttribute(eq("recipe"), recipeArgumentCaptor.capture());
    }

    @Test
    public void getRecipeByIdNotFound() throws Exception {

        when(recipeService.getRecipeById(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name("404error"));
    }



    @Test
    public void saveRecipe() throws Exception {

        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/recipeform"))
                .andExpect(model().attributeExists("newrecipe"));

        String str = recipeController.saveRecipe(model);
        assertEquals("recipes/recipeform", str);
        verify(model, times(1)).addAttribute(eq("newrecipe"),any());

    }

    @Test
    public void updateRecipe() throws Exception {


        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("2");
        when(recipeService.getRecipeCommandById(any())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/recipeform"))
                .andExpect(model().attributeExists("newrecipe"));



        assertEquals("recipes/recipeform", recipeController.updateRecipe(model,"1"));
        verify(model,times(1)).addAttribute(eq("newrecipe"),any());
        verify(recipeService, times(2)).getRecipeCommandById(any());


    }

    @Test
    public void saveOrUpdate() throws Exception{

        RecipeCommand command = new RecipeCommand();
        command.setId("2");

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "very good")
                .param("directions", "cook it")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/show"));
    }

    @Test
    public void saveOrUpdateValidationFail() throws Exception{

        mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "very good")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("newrecipe"))
                .andExpect(view().name("recipes/recipeform"));
    }


    @Test
    public void deleteById() throws Exception {

        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService, times(1)).deleteById(anyString());

    }
}