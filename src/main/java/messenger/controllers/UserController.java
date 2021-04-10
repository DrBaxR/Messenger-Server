package messenger.controllers;

import messenger.entities.Group;
import messenger.repositories.GroupRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import messenger.entities.User;
import messenger.exceptions.UserNotFoundException;
import messenger.other.UserModelAssembler;
import messenger.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin("*")
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserModelAssembler userAssembler;

    UserController(UserRepository userRepository, GroupRepository groupRepository, UserModelAssembler userAssembler){
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userAssembler = userAssembler;
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> allUsers(){
        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(userAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users,
            linkTo(methodOn(UserController.class).allUsers()).withSelfRel());
    }

    @PostMapping("/users")
    public EntityModel<User> newUser(@RequestBody User user){
        return userAssembler.toModel(userRepository.save(user));

    }

    @GetMapping("/users/{id}")
    public EntityModel<User> oneUser(@PathVariable String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userAssembler.toModel(user);
    }

    @PutMapping("/users/{id}")
    public EntityModel<User> replaceUser(@RequestBody User newUser, @PathVariable() String id){
        return userRepository.findById(id)
                .map(user -> {
                    if(newUser.getEmail() != null)
                        user.setEmail(newUser.getEmail());
                    if(newUser.getUsername() != null)
                        user.setUsername(newUser.getUsername());
                    if(newUser.getPassword() != null)
                        user.setPassword(newUser.getPassword());
                    if(newUser.getGroups() != null)
                        user.setGroups(newUser.getGroups());
                    return userAssembler.toModel(userRepository.save(user));
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userAssembler.toModel(userRepository.save(newUser));
                });
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable String id){
        if(!userRepository.existsById(id))
            throw new UserNotFoundException(id);

        userRepository.deleteById(id);
    }

    @GetMapping("/users/{id}/groups")
    public List<Group> allGroups(@PathVariable String id){
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
   public Group addGroup(@RequestBody Group group, @PathVariable String id)
   {
       User user = userRepository.findById(id)
               .orElseThrow(() -> new UserNotFoundException(id));

       group.addUser(id);
       Group newGroup = groupRepository.save(group);
       user.addGroup(newGroup.getId());
       userRepository.save(user);

       return newGroup;
   }


}
