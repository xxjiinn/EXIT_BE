package club.pard.exit.exception;

import club.pard.exit.actionitem.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.StringJoiner;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<?>> handleValidationException(MethodArgumentNotValidException e)
    {
        StringJoiner allErrors = new StringJoiner(" / ");
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            allErrors.add(String.format("%s: %s", fieldName, errorMessage));
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Response.setFailure("올바르지 않은 값이 들어왔어요!", allErrors.toString()));
    }
}
