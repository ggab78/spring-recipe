package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.domain.UnitOfMeasure;

import java.util.Set;

public interface UnitOfMeasureService {

    Set<UnitOfMeasure> findAllUom();

    Set<UnitOfMeasureCommand> findAllUomCommand();

}
