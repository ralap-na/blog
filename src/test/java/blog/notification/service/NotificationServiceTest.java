package blog.notification.service;

import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import blog.notification.entity.Notification;
import blog.repository.FakeNotificationRepository;
import blog.repository.FakeUserRepository;
import blog.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationServiceTest {

    private final FakeNotificationRepository notificationRepository = new FakeNotificationRepository();
    private final FakeUserRepository userRepository = new FakeUserRepository();

    private NotificationService notificationService;

    private final String user1Id = UUID.randomUUID().toString();
    private final String user2Id = UUID.randomUUID().toString();
    private final List<String> userIds = List.of(user1Id, user2Id);
    private final String title = "Congratulations!";
    private final String content = "Your article just hit 100 likes!";

    @BeforeEach
    public void setUp() {
        notificationService = new NotificationService(notificationRepository, userRepository);
        notificationRepository.clear();
        userRepository.save(new User(user1Id, "user1", "password"));
        userRepository.save(new User(user2Id, "user2", "password"));
    }

    @Test
    public void notifyUser() {
        OperationOutcome outcome = notificationService.notifyUser(user1Id, title, content, Instant.now());
        assertEquals(OutcomeState.SUCCESS, outcome.getState());
    }

    @Test
    public void notifyUsers() {
        OperationOutcome outcome = notificationService.notifyUsers(userIds, title, content, Instant.now());
        assertEquals(OutcomeState.SUCCESS, outcome.getState());
    }

    @Test
    public void getUserNotifications() {
        notificationService.notifyUser(user1Id, title, content, Instant.now());
        List<Notification> notifications = notificationService.getUserNotifications(user1Id);

        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
        assertEquals(user1Id, notifications.get(0).getUserId());
    }

    @Test
    public void getNotification() {
        OperationOutcome outcome = notificationService.notifyUser(user1Id, title, content, Instant.now());
        String notificationId = outcome.getId();

        Optional<Notification> notification = notificationService.getNotification(notificationId);
        assertTrue(notification.isPresent());
        assertEquals(notificationId, notification.get().getNotificationId());
    }

    @Test
    public void removeNotification() {
        OperationOutcome outcome = notificationService.notifyUser(user1Id, title, content, Instant.now());
        String notificationId = outcome.getId();

        OperationOutcome deleteOutcome = notificationService.removeNotification(notificationId);
        System.out.println(deleteOutcome.getMessage());
        assertEquals(OutcomeState.SUCCESS, deleteOutcome.getState());

        Optional<Notification> notification = notificationService.getNotification(notificationId);
        assertTrue(notification.isEmpty());
    }
}
