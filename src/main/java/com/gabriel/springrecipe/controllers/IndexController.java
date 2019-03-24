package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.domain.Category;
import com.gabriel.springrecipe.domain.UnitOfMeasure;
import com.gabriel.springrecipe.repositories.CategoryRepository;
import com.gabriel.springrecipe.repositories.UnitOfMeasureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class IndexController {

    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @RequestMapping({"", "/", "index", "index.html"})
    public String getIndexPage(Model model) {

        Optional<Category> categoryOptional = categoryRepository.findByDescription("American");
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Teaspoon");

        model.addAttribute("category", "Category Id : " + categoryOptional.get().getId());
        model.addAttribute("unit_of_measure", "Unit_Of_Measure Id : " + unitOfMeasureOptional.get().getId());

        return "index";
    }

}
