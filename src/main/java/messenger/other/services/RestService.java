package messenger.other.services;

import messenger.controllers.GroupController;
import messenger.entities.Message;
import org.springframework.stereotype.Service;


@Service
public class RestService {
    private final GroupController groupController;

    public RestService(GroupController groupController) {
        this.groupController = groupController;
    }

    public Message postMessage(Message message, String groupId) {
        return groupController.addGroupMessage(message, groupId);
    }
}
