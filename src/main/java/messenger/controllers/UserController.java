package messenger.controllers;

import messenger.entities.Group;
import messenger.entities.PasswordReset;
import messenger.exceptions.PasswordResetNotFoundException;
import messenger.mail.EmailService;
import messenger.repositories.GroupRepository;
import messenger.repositories.PasswordResetRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import messenger.entities.User;
import messenger.exceptions.UserNotFoundException;
import messenger.other.UserModelAssembler;
import messenger.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin("*")
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserModelAssembler userAssembler;
    private final EmailService emailService;
    private final PasswordResetRepository resetRepository;
    private final PasswordEncoder encoder;

    private final String webURL = "https://messenger-web-pkfomy4bva-lm.a.run.app/";

    UserController(UserRepository userRepository, GroupRepository groupRepository, UserModelAssembler userAssembler, EmailService emailService, PasswordResetRepository resetRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userAssembler = userAssembler;
        this.emailService = emailService;
        this.resetRepository = resetRepository;
        this.encoder = encoder;
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> allUsers() {
        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(userAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).allUsers()).withSelfRel());
    }

    @PostMapping("/users")
    public EntityModel<User> newUser(@RequestBody User user) {
        return userAssembler.toModel(userRepository.save(user));

    }

    @GetMapping("/users/{id}")
    public EntityModel<User> oneUser(@PathVariable String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userAssembler.toModel(user);
    }

    @PutMapping("/users/{id}")
    public EntityModel<User> replaceUser(@RequestBody User newUser, @PathVariable() String id) {
        return userRepository.findById(id)
                .map(user -> {
                    if (newUser.getEmail() != null)
                        user.setEmail(newUser.getEmail());
                    if (newUser.getUsername() != null)
                        user.setUsername(newUser.getUsername());
                    if (newUser.getPassword() != null)
                        user.setPassword(newUser.getPassword());
                    if (newUser.getGroups() != null)
                        user.setGroups(newUser.getGroups());
                    return userAssembler.toModel(userRepository.save(user));
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userAssembler.toModel(userRepository.save(newUser));
                });
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable String id) {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException(id);

        userRepository.deleteById(id);
    }

    @GetMapping("/users/{id}/groups")
    public List<Group> allGroups(@PathVariable String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        List<Group> groups = user.getGroups().stream()
                .map(groupId -> groupRepository.findById(groupId).orElseGet(() -> {
                    user.removeGroup(groupId);
                    userRepository.save(user);

                    return null;
                }))
                .collect(Collectors.toList());

        return groups;
    }

    @PostMapping("/users/{id}/groups")
    public Group addGroup(@RequestBody Group group, @PathVariable String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        group.addUser(id);
        Group newGroup = groupRepository.save(group);
        user.addGroup(newGroup.getId());
        userRepository.save(user);

        return newGroup;
    }

    @GetMapping("/users/{email}/forgot-password")
    public ResponseEntity<String> forgotPassword(@PathVariable String email) {
        User user = userRepository.findByEmail(email);

        if (user == null)
            return new ResponseEntity<>("Email not found!", HttpStatus.NOT_FOUND);

        Optional<PasswordReset> resetOptional = resetRepository.findByEmail(email);

        PasswordReset finalReset;
        finalReset = resetOptional.orElseGet(() -> resetRepository.save(new PasswordReset(email)));
        Thread emailThread = new Thread(() -> emailService.sendMail(email, "Password Reset", "In order to reset your password, access the following link:\n" + webURL + "reset-password/" + finalReset.getId()));
        emailThread.start();

        return new ResponseEntity<>("Password reset email sent!", HttpStatus.OK);
    }

    @PutMapping("/users/reset-password/{passId}")
    public ResponseEntity<User> updatePassword(@PathVariable String passId, @RequestBody String newPassword) {
        PasswordReset reset = resetRepository.findById(passId)
                .orElseThrow(() -> new PasswordResetNotFoundException(passId));

        User user = userRepository.findByEmail(reset.getEmail());

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        user.setPassword(encoder.encode(newPassword));
        resetRepository.deleteById(passId);
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }
}
