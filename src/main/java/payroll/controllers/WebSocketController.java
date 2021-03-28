package payroll.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import payroll.entities.Message;
import payroll.other.services.RestService;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate template;
    private final RestService restService;

    public WebSocketController(SimpMessagingTemplate template, RestService restService) {
        this.restService = restService;
        this.template = template;
    }

    @MessageMapping("/group.chat/{groupId}")
    public void sendMessage(@Payload Message chatMessage, @DestinationVariable String groupId) {
        restService.postMessage(chatMessage, groupId);

        this.template.convertAndSend("/topic/group." + groupId, chatMessage);
    }
}
