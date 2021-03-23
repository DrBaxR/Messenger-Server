package payroll.controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payroll.entities.Group;
import payroll.entities.User;
import payroll.exceptions.GroupNotFoundException;
import payroll.exceptions.UserNotFoundException;
import payroll.other.UserModelAssembler;
import payroll.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserModelAssembler userAssembler;

    UserController(UserRepository userRepository, UserModelAssembler userAssembler){
        this.userRepository = userRepository;
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
    ResponseEntity<?> newUser(@RequestBody User newUser){
        EntityModel<User> userEntityModel = userAssembler.toModel(userRepository.save(newUser));

        return ResponseEntity
                .created(userEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(userEntityModel);
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> oneUser(@PathVariable String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userAssembler.toModel(user);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable() String id){
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(newUser.getEmail());
                    user.setUsername(newUser.getUsername());
                    user.setPassword(newUser.getPassword());
                    user.setGroups(newUser.getGroups());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable String id){
        if(!userRepository.existsById(id))
            throw new UserNotFoundException(id);

        userRepository.deleteById(id);
    }


    //TODO: Heavy load from there: get user's actual groups

    @GetMapping("/users/{id}/groups")
    public List<String> allGroups(@PathVariable String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return user.getGroups();
    }

   @PostMapping("/users/{id}/groups")
   public String addGroup(@RequestBody String group, @PathVariable String id)
   {
       User user = userRepository.findById(id)
               .orElseThrow(() -> new UserNotFoundException(id));

       user.addGroup(group);
       userRepository.save(user);

       return group;
   }


}
