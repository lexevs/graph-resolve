package org.lexgrid.lexgraph.error;

import java.time.LocalDateTime;

import org.lexgrid.lexgraph.exception.GraphDbNotFoundException;
import org.lexgrid.lexgraph.exception.LexArangonDataAccessException;
import org.lexgrid.lexgraph.exception.SystemMetadataNotFoundException;
import org.lexgrid.lexgraph.exception.VertexNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    
    @ExceptionHandler(VertexNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex, WebRequest request) {

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());
        
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);

    }
    
    @ExceptionHandler(GraphDbNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> customGraphDbNotFound(Exception ex, WebRequest request) {

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());
        
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);

    }
    
    @ExceptionHandler(SystemMetadataNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> customSystemMetadataNotFound(Exception ex, WebRequest request) {

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());
        
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);

    }
    
    @ExceptionHandler(LexArangonDataAccessException.class)
    public ResponseEntity<CustomErrorResponse> customDataAccessExceptin(Exception ex, WebRequest request) {

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}