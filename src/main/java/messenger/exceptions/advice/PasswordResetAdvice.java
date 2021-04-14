package messenger.exceptions.advice;

import messenger.exceptions.PasswordResetNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PasswordResetAdvice {

    @ResponseBody
    @ExceptionHandler(PasswordResetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String resetNotFoundHandler(PasswordResetNotFoundException e) {
        return e.getMessage();
    }
}
