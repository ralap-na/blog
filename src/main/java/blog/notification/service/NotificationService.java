package blog.notification.service;

import blog.article.Repository;
import blog.notification.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private Repository repository;

    public boolean notifyUser(String userId, String articleId, String title, String content, Instant date) {
        if (repository.findUserById(userId) == null) {
            return false;
        }
        if (repository.findArticleById(articleId) == null){
            return false;
        }
        if (title == null || content == null || date == null) {
            return false;
        }

        Notification notification = new Notification(UUID.randomUUID().toString(), userId, articleId, title, content, date);

        repository.saveNotification(notification);
        return true;
    }

    public boolean notifyUsers(List<String> userIds, String articleId, String title, String content, Instant date) {
        for (String userId : userIds) {
            if (!notifyUser(userId, articleId, title, content, date)) {
                return false;
            }
        }
        return true;
    }

    public List<Notification> getUserNotifications(String userId) {
        return repository.findNotificationsByUserId(userId);
    }

    public Notification getNotification(String notificationId) {
        return repository.findNotificationById(notificationId);
    }

    public boolean removeNotification(String notificationId) {
        repository.deleteNotificationById(notificationId);
        return true;
    }
}
