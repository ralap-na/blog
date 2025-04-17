package blog.notification;

import blog.notification.entity.Notification;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotificationTest {
    @Test
    public void create() {
        String notificationId = UUID.randomUUID().toString();
        String articleId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String title = "test title";
        String content = "test content";
        Instant date = Instant.now();

        Notification notification = new Notification(notificationId, userId, articleId, title, content, date);

        assertEquals(notificationId, notification.getNotificationId());
        assertEquals(userId, notification.getUserId());
        assertEquals(articleId, notification.getArticleId());
        assertEquals(title, notification.getTitle());
        assertEquals(content, notification.getContent());
        assertEquals(date, notification.getDate());
    }
}
