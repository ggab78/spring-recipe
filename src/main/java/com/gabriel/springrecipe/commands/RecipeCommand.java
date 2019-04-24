package com.gabriel.springrecipe.commands;

import com.gabriel.springrecipe.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {

    private Long id;
    private String description;
    private String source;
    private String url;
    private String directions;
    private Integer cookTime;
    private Integer prepTime;
    private Integer servings;
    private Difficulty difficulty;
    private NotesCommand notes;
    private Set<IngredientCommand> ingredients = new HashSet<>();
    private Set<CategoryCommand> categories = new HashSet<>();
    private Byte[] image;

}
