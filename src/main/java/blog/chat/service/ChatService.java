package blog.chat.service;

import blog.article.Repository;
import blog.chat.Chat;
import blog.chat.Conversation;
import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class ChatService {
    @Autowired
    private Repository repository;

    public ChatService() {}
    public ChatService(Repository repository) {
        this.repository = repository;
    }

    public OperationOutcome chat(String user1Id, String user2Id) {
        if (user1Id == null || user2Id == null) {
            return OperationOutcome.create()
                    .setState(OutcomeState.FAILURE)
                    .setMessage("Users can't be empty!");
        }
        if (user1Id.equals(user2Id)) {
            return OperationOutcome.create()
                    .setState(OutcomeState.FAILURE)
                    .setMessage("Users can't be the same!");
        }
        if (repository.findUserById(user1Id) == null || repository.findUserById(user2Id) == null) {
            return OperationOutcome.create()
                    .setState(OutcomeState.FAILURE)
                    .setMessage("Users should exist!");
        }
        if (repository.findChatsByUsers(user1Id, user2Id)){
            return OperationOutcome.create()
                    .setState(OutcomeState.FAILURE)
                    .setMessage("You are already in this chat!");
        }

        String chatId = UUID.randomUUID().toString();
        Chat chat = new Chat(chatId, user1Id, user2Id);
        repository.saveChat(chat);

        return OperationOutcome.create()
                .setId(chatId)
                .setState(OutcomeState.SUCCESS)
                .setMessage("Chat users success!");
    }

    public OperationOutcome send(String chatId, String userId, String content, Instant date) {
        if (chatId == null || userId == null || content.isEmpty() || date == null) {
            return OperationOutcome.create()
                    .setState(OutcomeState.FAILURE)
                    .setMessage("All fields can't be empty!");
        }
        if (repository.findChat(chatId) == null) {
            return OperationOutcome.create()
                    .setState(OutcomeState.FAILURE)
                    .setMessage("Chat does not exist!");
        }
        if (repository.findUserById(userId) == null) {
            return OperationOutcome.create()
                    .setState(OutcomeState.FAILURE)
                    .setMessage("User does not exist!");
        }
        if (date.isAfter(Instant.now())) {
            return OperationOutcome.create()
                    .setState(OutcomeState.FAILURE)
                    .setMessage("Date is after now!");
        }

        String conversationId = UUID.randomUUID().toString();
        Conversation conversation = new Conversation(conversationId , userId, content, date);
        repository.findChat(chatId).addConversation(conversation);

        return OperationOutcome.create()
                .setId(conversationId)
                .setState(OutcomeState.SUCCESS)
                .setMessage("send conversation success!");
    }

    public Map<String, Chat> getAllChats(String userId) {
        if (userId == null) {
            return null;
        }
        return repository.findChatByUserId(userId);
    }
}