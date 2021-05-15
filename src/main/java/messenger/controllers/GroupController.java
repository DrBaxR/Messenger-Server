package messenger.controllers;

import messenger.exceptions.UserNotFoundException;
import messenger.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
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
                    if (newGroup.getMessages() != null)
                        group.setMessages(newGroup.getMessages());

                    if (newGroup.getUsers() != null)
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
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        group.getMessages().forEach(messageRepository::deleteById);

        group.getUsers().forEach(userId -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));
            user.removeGroup(id);
            userRepository.save(user);
        });

        groupRepository.deleteById(id);
    }

    @GetMapping("/groups/{id}/messages")
    public List<Message> allGroupMessages(
            @PathVariable String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        List<String> messageIds = group.getMessages();
        Collections.reverse(messageIds);
        int startIndex = Math.min(page*size, messageIds.size());
        int endIndex = Math.min(page*size + size, messageIds.size());

        return messageIds.subList(startIndex, endIndex).stream()
                .map(mId -> messageRepository.findById(mId).orElseGet(() -> {
                    group.removeMessage(mId);
                    groupRepository.save(group);

                    return null;
                }))
                .collect(Collectors.toList());
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
    public User addGroupUser(@RequestBody String userEmail, @PathVariable String id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        User user = userRepository.findByEmail(userEmail);

        if(user == null)
            throw new UserNotFoundException(userEmail);

        if(!user.getGroups().contains(id)) {
            user.addGroup(id);
            user = userRepository.save(user);
        }

        if(!group.getUsers().contains(user.getId())) {
            group.addUser(user.getId());
            groupRepository.save(group);
        }

        return user;
    }

    @PutMapping("/groups/{id}/users")
    public List<User> updateUsers(@RequestBody List<String> userIds, @PathVariable String id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        if (userIds != null) {
            group.setUsers(userIds);
            groupRepository.save(group);
        }

        return allGroupUsers(id);
    }
}
