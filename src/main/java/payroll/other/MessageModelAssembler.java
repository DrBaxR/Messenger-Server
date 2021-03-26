package payroll.other;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import payroll.controllers.MessageController;
import payroll.controllers.UserController;
import payroll.entities.Message;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MessageModelAssembler implements RepresentationModelAssembler<Message, EntityModel<Message>> {
    @Override
    public EntityModel<Message> toModel(Message message) {
        return EntityModel.of(message
                ,
                linkTo(methodOn(MessageController.class).getMessageById(message.getId())).withSelfRel(),
                linkTo(methodOn(MessageController.class).allMessages()).withRel("messages"));
    }
}
