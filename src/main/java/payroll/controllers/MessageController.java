package payroll.controllers;

import org.springframework.web.bind.annotation.*;
import payroll.entities.Message;
import payroll.exceptions.MessageNotFoundException;
import payroll.repositories.MessageRepository;
import java.util.List;

@RestController
public class MessageController {


    private final MessageRepository repository;
    public MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/messages")
    List<Message> allMessages() {
        return repository.findAll();
    }

    @PostMapping("/messages")
    Message postMessage(@RequestBody Message message){
        return repository.save(message);
    }

    @GetMapping("/messages/{id}")
    Message getMessageById(@PathVariable String id){
        return  repository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
    }
    @DeleteMapping("/messages/{id}")
    void deleteMessageById(@PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }else throw new MessageNotFoundException(id);
    }




}
