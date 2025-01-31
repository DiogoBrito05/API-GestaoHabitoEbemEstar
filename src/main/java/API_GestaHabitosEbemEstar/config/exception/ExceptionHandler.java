package API_GestaHabitosEbemEstar.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandler {
    

  public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }

    public static class NotAuthorizedException extends RuntimeException {
        public NotAuthorizedException(String message) {
            super(message);
        }
    }

    public static class Conflict extends RuntimeException {
        public Conflict(String message) {
            super(message);
        }
    }

    public static class RuntimeError extends RuntimeException {
        public RuntimeError(String message) {
            super(message);
        }
    }

// ===============================================================================================
                                /* Funções genérica que trata as exceções */ 

    @org.springframework.web.bind.annotation.ExceptionHandler({
        NotFoundException.class,
        BadRequestException.class,
        NotAuthorizedException.class,
        Conflict.class,
        RuntimeError.class
    })
    public ResponseEntity<?> handleCustomExceptions(RuntimeException ex, WebRequest request) {
        HttpStatus status;

        if (ex instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof BadRequestException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof NotAuthorizedException) {
            status = HttpStatus.NON_AUTHORITATIVE_INFORMATION;
        } else if (ex instanceof Conflict) {
            status = HttpStatus.CONFLICT;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(ex.getMessage());
    }
}



