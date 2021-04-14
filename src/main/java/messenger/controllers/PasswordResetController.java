package messenger.controllers;

import messenger.entities.PasswordReset;
import messenger.exceptions.PasswordResetNotFoundException;
import messenger.repositories.PasswordResetRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class PasswordResetController {
    private final PasswordResetRepository resetRepository;

    public PasswordResetController(PasswordResetRepository resetRepository) {
        this.resetRepository = resetRepository;
    }

    @GetMapping("/resets/{id}")
    public PasswordReset getOne(@PathVariable String id) {
        PasswordReset reset = resetRepository.findById(id)
                .orElseThrow(() -> new PasswordResetNotFoundException(id));

        return reset;
    }
}
