package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.domain.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ImageServiceImplTest {

    ImageService imageService;

    @Mock
    RecipeService recipeService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        imageService = new ImageServiceImpl(recipeService);
    }

    @Test
    public void saveImageFile() throws Exception{

        //given
        String id="1";
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework".getBytes());
        Recipe recipe = new Recipe();
        recipe.setId(id);
        when(recipeService.getRecipeById(anyString())).thenReturn(Mono.just(recipe));
        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        //when
        imageService.saveImageFile(id,multipartFile);


        //then
        verify(recipeService, times(1)).getRecipeById(anyString());
        verify(recipeService, times(1)).saveRecipe(captor.capture());

        Recipe capturedRecipe = captor.getValue();
        assertEquals(multipartFile.getBytes().length, capturedRecipe.getImage().length);


    }
}