package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.domain.Recipe;
import com.gabriel.springrecipe.services.RecipeService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Ignore
public class IndexControllerTest {

    IndexController indexController;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeService);
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        when(recipeService.getRecipes()).thenReturn(Flux.empty());

        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/index"))
                .andExpect(model().attributeExists("recipes"));
    }

    @Test
    public void getRecipeList() {

        Set<Recipe> recipes = new HashSet<>();
        Recipe recipe = new Recipe();
        recipe.setId("1");
        recipes.add(recipe);

        when(recipeService.getRecipes()).thenReturn(Flux.fromIterable(recipes));

        ArgumentCaptor<List<Recipe>> argumentCaptor = ArgumentCaptor.forClass(List.class);


        //when
        String str = indexController.getRecipeList(model);

        //then
        assertEquals("recipes/index", str);

        verify(recipeService, times(1)).getRecipes();
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());

        List<Recipe> recipeList = argumentCaptor.getValue();

        assertEquals(1, recipeList.size());

    }

}