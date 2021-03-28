package payroll.other.services;

import org.springframework.stereotype.Service;
import payroll.controllers.GroupController;
import payroll.entities.Message;

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
