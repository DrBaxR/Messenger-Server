package messenger.controllers;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import messenger.entities.Message;
import messenger.entities.User;
import messenger.exceptions.GroupNotFoundException;
import messenger.other.GroupModelAssembler;
import messenger.repositories.GroupRepository;
import messenger.entities.Group;
import messenger.repositories.MessageRepository;
import messenger.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GroupController {

    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final GroupModelAssembler assembler;

    public GroupController(GroupRepository groupRepository, MessageRepository messageRepository, UserRepository userRepository, GroupModelAssembler assembler) {
        this.groupRepository = groupRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.assembler = assembler;
    }

    @GetMapping("/groups")
    public List<EntityModel<Group>> allGroups() {
        return groupRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    @PostMapping("/groups")
    public EntityModel<Group> newGroup(@RequestBody Group group) {
        return assembler.toModel(groupRepository.save(group));
    }

    @GetMapping("/groups/{id}")
    public EntityModel<Group> oneGroup(@PathVariable String id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        return assembler.toModel(group);
    }

    @PutMapping("/groups/{id}")
    public EntityModel<Group> replaceGroup(@RequestBody Group newGroup, @PathVariable String id) {
        return groupRepository.findById(id)
                .map(group -> {
                    if(newGroup.getMessages() != null)
                        group.setMessages(newGroup.getMessages());

                    if(newGroup.getUsers() != null)
                        group.setUsers(newGroup.getUsers());

                    return assembler.toModel(groupRepository.save(group));
                })
                .orElseGet(() -> {
                    newGroup.setId(id);
                    return assembler.toModel(groupRepository.save(newGroup));
                });
    }

    @DeleteMapping("/groups/{id}")
    public void deleteGroup(@PathVariable String id) {
        if (!groupRepository.existsById(id))
            throw new GroupNotFoundException(id);

        groupRepository.deleteById(id);
    }

    @GetMapping("/groups/{id}/messages")
    public List<Message> allGroupMessages(@PathVariable String id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        List<Message> messages = group.getMessages().stream()
                .map(mId -> messageRepository.findById(mId).orElseGet(() -> {
                    group.removeMessage(mId);
                    groupRepository.save(group);

                    return null;
                }))
                .collect(Collectors.toList());

        return messages;
    }

    @PostMapping("/groups/{id}/messages")
    public Message addGroupMessage(@RequestBody Message message, @PathVariable String id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        Message newMessage = messageRepository.save(message);
        group.addMessage(newMessage.getId());

        groupRepository.save(group);

        return newMessage;
    }

    @GetMapping("/groups/{id}/users")
    public List<User> allGroupUsers(@PathVariable String id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        List<User> users = group.getUsers().stream()
                .map(uId -> userRepository.findById(uId).orElseGet(() -> {
                    group.removeUser(uId);
                    groupRepository.save(group);

                    return null;
                }))
                .collect(Collectors.toList());

        return users;
    }

    @PostMapping("/groups/{id}/users")
    public User addGroupUser(@RequestBody User user, @PathVariable String id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        if(!user.getGroups().contains(group.getId()))
            user.addGroup(group.getId());
        User newUser = userRepository.save(user);
        group.addUser(newUser.getId());

        groupRepository.save(group);

        return newUser;
    }

    @PutMapping("/groups/{id}/users")
    public List<User> updateUsers(@RequestBody List<String> userIds, @PathVariable String id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        if(userIds != null) {
            group.setUsers(userIds);
            groupRepository.save(group);
        }

        return allGroupUsers(id);
    }
}
