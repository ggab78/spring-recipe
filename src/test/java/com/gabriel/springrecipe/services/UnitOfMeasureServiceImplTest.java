package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.gabriel.springrecipe.domain.UnitOfMeasure;
import com.gabriel.springrecipe.repositories.UnitOfMeasureRepository;
import com.gabriel.springrecipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class UnitOfMeasureServiceImplTest {

    UnitOfMeasureServiceImpl unitOfMeasureService;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @Mock
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    MockMvc mockMvc;
    Flux<UnitOfMeasure> uoms;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureReactiveRepository, unitOfMeasureToUnitOfMeasureCommand);
        mockMvc = MockMvcBuilders.standaloneSetup(unitOfMeasureService).build();

        UnitOfMeasure unitOne = new UnitOfMeasure();
        unitOne.setId("1");

        UnitOfMeasure unitTwo = new UnitOfMeasure();
        unitTwo.setId("2");

        uoms = Flux.just(unitOne, unitTwo);

    }

    @Test
    public void findAllUom() {

        //when
        when(unitOfMeasureReactiveRepository.findAll()).thenReturn(uoms);

        //then
        assertEquals((Long)2L, unitOfMeasureService.findAllUom().count().block());
        assertEquals("1", unitOfMeasureService.findAllUom().collectList().block().get(0).getId());

    }

    @Test
    public void findAllUomCommand() {
        //given
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();

        //when
        when(unitOfMeasureReactiveRepository.findAll()).thenReturn(uoms);
        when(unitOfMeasureToUnitOfMeasureCommand.convert(any())).thenReturn(unitOfMeasureCommand);

        //then
        assertEquals((Long)2L,unitOfMeasureService.findAllUom().count().block());

        //unitofMeasureCommand is the same that is why only one is added and invoked twice
        assertEquals((Long)2L, unitOfMeasureService.findAllUomCommand().count().block());
        verify(unitOfMeasureToUnitOfMeasureCommand, times(2)).convert(any());
    }
}