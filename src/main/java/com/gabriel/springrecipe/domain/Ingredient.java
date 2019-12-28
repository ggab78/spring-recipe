package com.gabriel.springrecipe.domain;

import lombok.*;
import lombok.EqualsAndHashCode.Exclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Ingredient {



    private String id = UUID.randomUUID().toString();

//    need to exclude recipes from hashCode because of bidirectional mapping
//    @Exclude
//    private Recipe recipe;

    @DBRef
    private UnitOfMeasure uom;
    private String description;
    private BigDecimal amount;

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom){
        this.uom=uom;
        this.description=description;
        this.amount=amount;
    }

}
