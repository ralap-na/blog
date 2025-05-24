package blog.chat.controller;

import blog.chat.Chat;
import blog.chat.Message;
import blog.chat.service.ChatService;
import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Controller
@RequestMapping("/chats")
public class ChatController {
    @Autowired
    ChatService chatService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Chat>> getAllChat(@PathVariable("userId") String userId) {
        Map<String, Chat> chats = chatService.getAllChats(userId);
        if (chats != null) {
            return ResponseEntity.ok(chats);
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/chat")
    public ResponseEntity<String> createChat(@RequestBody String users) {
        JSONObject jsonObject = new JSONObject(users);
        String user1Id = jsonObject.getString("user1Id");
        String user2Id = jsonObject.getString("user2Id");

        OperationOutcome outcome = chatService.chat(user1Id, user2Id);
        if (outcome.getState().equals(OutcomeState.SUCCESS)) {
            return ResponseEntity.ok(outcome.getId());
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{chatId}")
    public ResponseEntity<String> send(@PathVariable("chatId") String chatId, @RequestBody String data) throws IOException {
        JSONObject jsonObject = new JSONObject(data);
        String userId = jsonObject.getString("userId");
        String content = jsonObject.getString("content");
        Instant now = Instant.now();

        OperationOutcome outcome = chatService.send(chatId, userId, content, now);
        if (outcome.getState().equals(OutcomeState.SUCCESS)) {
            Message message = new Message(outcome.getId(), userId, content, now);
            messagingTemplate.convertAndSend("/topic/chat/" + chatId, message);
            return ResponseEntity.ok(outcome.getId());
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
}