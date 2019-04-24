package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.services.ImageService;
import com.gabriel.springrecipe.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@AllArgsConstructor
public class ImageController {

    RecipeService recipeService;
    ImageService imageService;

    @RequestMapping("/recipe/{recipeId}/image")
    public String getImageForm(@PathVariable String recipeId, Model model) {
        model.addAttribute("recipe", recipeService.getRecipeCommandById(Long.parseLong(recipeId)));
        return "recipes/imageuploadform";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String handleImage(@PathVariable String recipeId, @RequestParam("imagefile")MultipartFile file){

        imageService.saveImageFile(Long.parseLong(recipeId), file);

        return "redirect:/recipe/"+recipeId+"/show";
    }

}
