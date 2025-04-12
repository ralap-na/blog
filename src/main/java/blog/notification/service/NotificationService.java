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

    public boolean notifyUser(String userId, String title, String content, Instant date) {
//        if (repository.findUserById() == null) {
//            return false;
//        }
        if (title == null || content == null || date == null) {
            return false;
        }

        Notification notification = new Notification(UUID.randomUUID().toString(), userId, title, content, date);

        repository.saveNotification(notification);
        return true;
    }

    public boolean notifyUsers(List<String> userIds, String title, String content, Instant date) {
        for (String userId : userIds) {
            if (!notifyUser(userId, title, content, date)) {
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
