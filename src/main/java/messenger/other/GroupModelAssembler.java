package messenger.other;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import messenger.controllers.GroupController;
import messenger.entities.Group;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GroupModelAssembler implements RepresentationModelAssembler<Group, EntityModel<Group>> {

    @Override
    public EntityModel<Group> toModel(Group group) {
        return EntityModel.of(group,
                linkTo(methodOn(GroupController.class).oneGroup(group.getId())).withSelfRel(),
                linkTo(methodOn(GroupController.class).allGroups()).withRel("groups"),
                linkTo(methodOn(GroupController.class).allGroupUsers(group.getId())).withRel("users"),
                linkTo(methodOn(GroupController.class).allGroupMessages(group.getId())).withRel("messages"));
    }
}
