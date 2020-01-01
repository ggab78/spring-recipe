package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.services.ImageService;
import com.gabriel.springrecipe.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {

    ImageController imageController;

    @Mock
    RecipeService recipeService;

    @Mock
    ImageService imageService;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        imageController = new ImageController(recipeService, imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void getImageForm() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");
        when(recipeService.getRecipeCommandById(anyString())).thenReturn(Mono.just(recipeCommand));

        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipes/imageuploadform"));

        verify(recipeService,times(1)).getRecipeCommandById(anyString());
    }


    @Test
    public void handleImage() throws Exception {

        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework".getBytes());

        when(imageService.saveImageFile(anyString(),any())).thenReturn(Mono.empty());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService, times(1)).saveImageFile(anyString(), any());
    }

    @Test
    public void renderImageFromDB () throws Exception{

        //given
        RecipeCommand command = new RecipeCommand();
        command.setId("1");
        String s = "fake image";
        Byte[] bytes = new Byte[s.getBytes().length];

        int i=0;
        for(byte b : s.getBytes()){
            bytes[i++] = b;
        }
        command.setImage(bytes);

        when(recipeService.getRecipeCommandById(anyString())).thenReturn(Mono.just(command));

        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = response.getContentAsByteArray();

        assertEquals(s.getBytes().length, responseBytes.length);

    }
}