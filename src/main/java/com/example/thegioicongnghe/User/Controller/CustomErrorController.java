package com.example.thegioicongnghe.User.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundError(Exception ex, Model model) {
        model.addAttribute("error", "Page not found");
        model.addAttribute("message", ex.getMessage());
        return "404"; // Tên file template 404.html
    }
}
