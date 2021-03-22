package payroll.controllers;

import org.springframework.web.bind.annotation.*;
import payroll.entities.Message;
import payroll.exceptions.GroupNotFoundException;
import payroll.repositories.GroupRepository;
import payroll.entities.Group;

import java.util.List;

@RestController
public class GroupController {

    // TODO: Work on making this hypermedia based
    private final GroupRepository repository;

    public GroupController(GroupRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/groups")
    List<Group> allGroups() {
        return repository.findAll();
    }

    @PostMapping("/groups")
    Group newGroup(@RequestBody Group group) {
        return repository.save(group);
    }

    @GetMapping("/groups/{id}")
    Group oneGroup(@PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));
    }

    @PutMapping("/groups/{id}")
    Group replaceGroup(@RequestBody Group newGroup, @PathVariable String id) {
        return repository.findById(id)
                .map(group -> {
                    group.setMessages(newGroup.getMessages());
                    group.setUsers(newGroup.getUsers());

                    return repository.save(group);
                })
                .orElseGet(() -> {
                    newGroup.setId(id);
                    return repository.save(newGroup);
                });
    }

    @DeleteMapping("/groups/{id}")
    void deleteGroup(@PathVariable String id) {
        if (!repository.existsById(id))
            throw new GroupNotFoundException(id);

        repository.deleteById(id);
    }

    //TODO: make this give the actual messages, not only the IDs
    @GetMapping("/groups/{id}/messages")
    List<Message> allGroupMessages(@PathVariable String id) {
        Group group = repository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        return group.getMessages();
    }

    @PostMapping("/groups/{id}/messages")
    Message addGroupMessage(@RequestBody Message message, @PathVariable String id) {
        // getting the group must be done first or else a message would be created even if the group does not exist
        Group group = repository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        // TODO: actually create the new message in the database (you will have to change the bodyParam to type message)
        // KEEP IN MIND: some message ids could no longer be valid (they were deleted)

        // add the newly created message to the list of messages in the group
        group.addMessage(message);
        repository.save(group);

        return message;
    }

    // TODO: same thing that were noted for messages
    @GetMapping("/groups/{id}/users")
    List<String> allGroupUsers(@PathVariable String id) {
        Group group = repository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        return group.getUsers();
    }

    @PostMapping("/groups/{id}/users")
    String addGroupUser(@RequestBody String user, @PathVariable String id) {
        // get the group
        Group group = repository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        // TODO:

        // update the group
        group.addUser(user);
        repository.save(group);

        return user;
    }
}
