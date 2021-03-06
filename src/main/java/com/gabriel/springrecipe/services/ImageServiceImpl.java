package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.domain.Recipe;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    RecipeService recipeService;

    @Override
    public void saveImageFile(Long id, MultipartFile file) {

        Recipe recipe = recipeService.getRecipeById(id);

        try{
            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;
            for(byte b: file.getBytes()){
                byteObjects[i++]=b;
            }

            recipe.setImage(byteObjects);

            recipeService.saveRecipe(recipe);

        }catch (IOException e){
            log.error("error reading image", e);
            e.printStackTrace();
        }

    }
}
