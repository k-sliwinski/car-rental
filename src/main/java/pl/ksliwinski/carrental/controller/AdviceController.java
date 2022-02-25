package pl.ksliwinski.carrental.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.ksliwinski.carrental.exception.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return new ExceptionResponse(HttpStatus.NOT_FOUND, entityNotFoundException.getMessage());
    }

    @ExceptionHandler(value = {CarIsRentedException.class, ConstraintViolationException.class, SQLIntegrityConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBadRequestException(RuntimeException exception) {
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(value = {CarIsNotRentedByThisUserException.class, EmailAlreadyTakenException.class, UserIsStillRentingCarsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleConflictException(RuntimeException exception) {
        return new ExceptionResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> constraintViolations = e.getBindingResult().getAllErrors().stream()
                .map(er -> (FieldError) er)
                .collect(Collectors.toMap(FieldError::getField, fieldError ->
                        Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Validation failed for this field")));
        return new ValidationExceptionResponse(HttpStatus.BAD_REQUEST, "Validation failed for given fields", constraintViolations);
    }
}
