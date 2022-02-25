package pl.ksliwinski.carrental.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
public class ExceptionResponse {
    private final int status;
    private final String message;
    private final Instant timestamp = Instant.now();

    public ExceptionResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }
}
