package com.training.spring.bigcorp.controller;

import com.training.spring.bigcorp.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handle(NotFoundException e){
        ModelAndView mv = new ModelAndView("/error/404")
                .addObject("status", 404)
                .addObject("error", "Not found exception")
                .addObject("trace", e.getStackTrace().toString())
                .addObject("timestamp", new Date())
                .addObject("message", e.getMessage());
        mv.setStatus(HttpStatus.NOT_FOUND);
        return mv;
    }
}
