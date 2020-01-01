package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice

public class ControllerExceptionHandler {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(NumberFormatException.class)
//    public ModelAndView handleNumberFormatException(NumberFormatException exception){
//        log.error("Handling NumberFormatException "+ exception.getMessage());
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("exception", exception);
//        modelAndView.setViewName("400error");
//        return  modelAndView;
//    }
//
//
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(NotFoundException.class)
//    public ModelAndView handleNotFoundException(NotFoundException exception){
//        log.error("Handling NotFoundException "+ exception.getMessage());
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("exception", exception);
//        modelAndView.setViewName("404error");
//        return  modelAndView;
//    }
}
