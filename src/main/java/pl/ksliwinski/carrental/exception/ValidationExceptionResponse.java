package pl.ksliwinski.carrental.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ValidationExceptionResponse {
    private final int status;
    private final String message;
    private final Map<String, String> errors = new HashMap<>();
    private final Instant timestamp = Instant.now();

    public ValidationExceptionResponse(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status.value();
        this.message = message;
        this.errors.putAll(errors);
    }
}
