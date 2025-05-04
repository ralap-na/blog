package blog.chat;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatTest {
    @Test
    public void addConversation() {
        String user1Id = UUID.randomUUID().toString();
        String user2Id = UUID.randomUUID().toString();
        Chat chat = new Chat(UUID.randomUUID().toString(), user1Id, user2Id);
        chat.addConversation(new Conversation(UUID.randomUUID().toString(), user1Id,"hi!", Instant.now()));

        assertEquals(1, chat.getConversations().size());
        assertEquals(user1Id, chat.getConversations().getFirst().getUserId());
    }
}
