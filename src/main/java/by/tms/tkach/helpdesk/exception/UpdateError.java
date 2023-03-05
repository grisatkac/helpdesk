package by.tms.tkach.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UpdateError extends RuntimeException {

    public UpdateError(String message) {
        super(message);
    }
}
