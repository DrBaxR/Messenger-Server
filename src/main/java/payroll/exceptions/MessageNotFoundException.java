package payroll.exceptions;

public class MessageNotFoundException extends RuntimeException{
    public MessageNotFoundException(String id) {
        super("Could not find message " + id);
    }
}

