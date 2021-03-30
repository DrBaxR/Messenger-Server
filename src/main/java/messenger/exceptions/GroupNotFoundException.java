package messenger.exceptions;

public class GroupNotFoundException extends RuntimeException{
    public GroupNotFoundException(String id) {
        super("Could not find group " + id);
    }
}
