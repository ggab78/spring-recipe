package com.gabriel.springrecipe.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {


    @NotBlank
    private String description;
    private String id;
    private String recipeId;

    //@NotNull
    private UnitOfMeasureCommand uom;

    //@NotNull
    //@Min(1)
    private BigDecimal amount;

}
