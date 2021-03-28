package payroll.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import payroll.entities.Message;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate template;

    public WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/group.chat/{groupId}")
    public void sendMessage(@Payload Message chatMessage, @DestinationVariable String groupId) {
        // save message to database


        this.template.convertAndSend("/topic/group." + groupId, chatMessage);
    }
}
