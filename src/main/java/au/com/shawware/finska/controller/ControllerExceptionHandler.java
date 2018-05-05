/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import au.com.shawware.util.persistence.PersistenceException;

/**
 * Exception handler for REST controllers.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@ControllerAdvice
@SuppressWarnings("static-method")
public class ControllerExceptionHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

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
        LOG.error("Illegal argument exception: " + e.getMessage(), e); //$NON-NLS-1$
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles a persistence error.
     *  
     * @param e the actual exception
     * @param request the web request that caused the error
     * 
     * @return The response entity.
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<String> handlePersistenceError(PersistenceException e, WebRequest request)
    {
        LOG.error("Persistence exception: " + e.getMessage(), e); //$NON-NLS-1$
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
