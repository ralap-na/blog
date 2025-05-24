package blog.chat;

import blog.chat.service.ChatService;
import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import blog.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ChatServiceTest {
    private final ChatService chatService;

    private final String user1Id;
    private final String user2Id;

    public ChatServiceTest(){
        FakeRepository fakeRepository = new FakeRepository();
        chatService = new ChatService(fakeRepository);

        user1Id = UUID.randomUUID().toString();
        user2Id = UUID.randomUUID().toString();
        fakeRepository.saveUser(new User(user1Id, "user1", UUID.randomUUID().toString()));
        fakeRepository.saveUser(new User(user2Id, "user2", UUID.randomUUID().toString()));
    }

    @Test
    public void chat() {
        OperationOutcome outcome = chatService.chat(user1Id, user2Id);
        assertEquals(OutcomeState.SUCCESS, outcome.getState());
    }

    @Test
    public void send() {
        OperationOutcome chatOutcome = chatService.chat(user1Id, user2Id);
        OperationOutcome outcome = chatService.send(chatOutcome.getId(), user1Id, "hi!", Instant.now());
        assertEquals(OutcomeState.SUCCESS, outcome.getState());
    }
}
