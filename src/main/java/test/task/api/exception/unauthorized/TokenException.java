package test.task.api.exception.unauthorized;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TokenException extends ResponseStatusException {
    public TokenException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
