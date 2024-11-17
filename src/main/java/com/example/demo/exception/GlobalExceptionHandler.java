package com.example.demo.exception;

import com.example.demo.handler.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Обработка исключения ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Обработка исключения ValidationException
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidation(ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Обработка исключения AuthenticationException
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthentication(AuthenticationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Общая обработка исключений с формированием сообщения
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception e) {
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Обработка исключения TaskNotFoundException
    @ExceptionHandler(value = TaskNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> taskNotFoundHandler(TaskNotFoundException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.NOT_FOUND, ex, webRequest);
    }

    // Обработка исключения UserNotAuthorizedException
    @ExceptionHandler(value = UserNotAuthorizedException.class)
    public ResponseEntity<ErrorResponseBody> userNotAuthorizedHandler(UserNotAuthorizedException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.FORBIDDEN, ex, webRequest);
    }

    // Обработка исключения CommentNotFoundException
    @ExceptionHandler(value = CommentNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> commentNotFoundHandler(CommentNotFoundException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.NOT_FOUND, ex, webRequest);
    }

    // Обработка исключения UserAlreadyExistException
    @ExceptionHandler(value = UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponseBody> userAlreadyExistHandler(UserAlreadyExistException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex, webRequest);
    }

    // Общая обработка исключений для других типов ошибок
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponseBody> generalExceptionHandler(Exception ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, webRequest);
    }

    // Вспомогательный метод для формирования ответа с ошибкой
    private ResponseEntity<ErrorResponseBody> buildResponse(HttpStatus httpStatus, Exception ex, WebRequest webRequest) {
        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResponseBody.builder()
                        .message(ex.getMessage())
                        .description(webRequest.getDescription(false))
                        .build());
    }
}

