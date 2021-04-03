package messenger.controllers;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import messenger.entities.Message;
import messenger.exceptions.GroupNotFoundException;
import messenger.exceptions.MessageNotFoundException;
import messenger.other.MessageModelAssembler;
import messenger.repositories.MessageRepository;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
public class MessageController {


    private final MessageRepository repository;
    private final MessageModelAssembler assembler;
    public MessageController(MessageRepository repository, MessageModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/messages")
    public List<EntityModel<Message>> allMessages() {
        return repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    @PostMapping("/messages")
    Message postMessage(@RequestBody Message message){
        return repository.save(message);
    }

    @GetMapping("/messages/{id}")
    public EntityModel<Message> getMessageById(@PathVariable String id){
        Message message = repository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));

        return assembler.toModel(message);
    }
    @DeleteMapping("/messages/{id}")
    void deleteMessageById(@PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }else throw new MessageNotFoundException(id);
    }




}
