package payroll.controllers;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import payroll.exceptions.GroupNotFoundException;
import payroll.other.GroupModelAssembler;
import payroll.repositories.GroupRepository;
import payroll.entities.Group;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GroupController {

    private final GroupRepository repository;
    private final GroupModelAssembler assembler;

    public GroupController(GroupRepository repository, GroupModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/groups")
    public List<EntityModel<Group>> allGroups() {
        return repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    @PostMapping("/groups")
    public EntityModel<Group> newGroup(@RequestBody Group group) {
        return assembler.toModel(repository.save(group));
    }

    @GetMapping("/groups/{id}")
    public EntityModel<Group> oneGroup(@PathVariable String id) {
        Group group = repository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        return assembler.toModel(group);
    }

    @PutMapping("/groups/{id}")
    public EntityModel<Group> replaceGroup(@RequestBody Group newGroup, @PathVariable String id) {
        return repository.findById(id)
                .map(group -> {
                    group.setMessages(newGroup.getMessages());
                    group.setUsers(newGroup.getUsers());

                    return assembler.toModel(repository.save(group));
                })
                .orElseGet(() -> {
                    newGroup.setId(id);
                    return assembler.toModel(repository.save(newGroup));
                });
    }

    @DeleteMapping("/groups/{id}")
    public void deleteGroup(@PathVariable String id) {
        if (!repository.existsById(id))
            throw new GroupNotFoundException(id);

        repository.deleteById(id);
    }

    //TODO: make this give the actual messages, not only the IDs
    @GetMapping("/groups/{id}/messages")
    public List<String> allGroupMessages(@PathVariable String id) {
        Group group = repository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        return group.getMessages();
    }

    @PostMapping("/groups/{id}/messages")
    public String addGroupMessage(@RequestBody String message, @PathVariable String id) {
        Group group = repository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        // TODO: actually create the new message in the database (you will have to change the bodyParam to type message)
        // KEEP IN MIND: some message ids could no longer be valid (they were deleted)
        group.addMessage(message);
        repository.save(group);

        return message;
    }

    // TODO: same thing that were noted for messages
    @GetMapping("/groups/{id}/users")
    public List<String> allGroupUsers(@PathVariable String id) {
        Group group = repository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        return group.getUsers();
    }

    @PostMapping("/groups/{id}/users")
    public String addGroupUser(@RequestBody String user, @PathVariable String id) {
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
