package com.VGS.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


    //GlobalExceptionHandler is a centralized error handler for the entire application.
@ControllerAdvice
    public class GlobalExceptionHandler {

    /* This method that is added handles all exceptions of type Exception (any unhandled errors).
     * @param e The exception that occurred.
     * @param model Model used to pass data to the view.
     * @return The name of the error template to render.
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        // Add a generic error message to the model for the error page.
        model.addAttribute("errorMessage", "Something went wrong. Please try again.");
        return "error"; // Returns the error.html template to display the message
    }
}
