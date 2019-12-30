package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.domain.UnitOfMeasure;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureService {
    Flux<UnitOfMeasure> findAllUom();
    Flux<UnitOfMeasureCommand> findAllUomCommand();
}
