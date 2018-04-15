/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Exception handler for REST controllers.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@ControllerAdvice
public class ControllerExceptionHandler
{
    /**
     * Handles an incorrect argument error. This typically arises when
     * a URL specifies an entity that does not exist.
     * 
     * @param e the actual exception
     * @param request the web request that caused the error
     * 
     * @return The response entity.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIncorrectArgument(IllegalArgumentException e, WebRequest request)
    {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
