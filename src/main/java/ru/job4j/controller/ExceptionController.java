package ru.job4j.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.job4j.exception.TaskNotFoundById;
import ru.job4j.exception.UserWithSuchLoginAlreadyExists;
import ru.job4j.exception.UserWithSuchLoginAndPasswordDoesNotExists;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UserWithSuchLoginAlreadyExists.class)
    public ModelAndView userWithSuchLoginAlreadyExists() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("alreadyExists", true);
        mv.setViewName("registrationPage");
        return mv;
    }

    @ExceptionHandler(UserWithSuchLoginAndPasswordDoesNotExists.class)
    public ModelAndView userWithSuchLoginAndPasswordDoesNotExists() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("userNotFound", true);
        mv.setViewName("loginPage");
        return mv;
    }

    @ExceptionHandler(TaskNotFoundById.class)
    public ModelAndView taskNotFound() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("taskNotFound");
        return mv;
    }
}
