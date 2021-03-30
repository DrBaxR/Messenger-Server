package messenger.exceptions.advice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import messenger.exceptions.GroupNotFoundException;
import messenger.exceptions.MessageNotFoundException;

@ControllerAdvice
public class MessageNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String groupNotFoundHandler(GroupNotFoundException exception) {
        return exception.getMessage();
    }
}