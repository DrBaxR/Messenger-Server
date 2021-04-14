package messenger.exceptions;

public class PasswordResetNotFoundException extends RuntimeException {
    public PasswordResetNotFoundException(String id) {
        super("No password reset with id " + id);
    }
}
