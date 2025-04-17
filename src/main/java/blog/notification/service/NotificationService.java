package blog.notification.service;

import blog.article.Repository;
import blog.common.OperationOutcome;
import blog.common.OutcomeState;
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

    public OperationOutcome notifyUser(String userId, String articleId, String title, String content, Instant date) {
        if (title == null || content == null || date == null) {
            return OperationOutcome
                    .create()
                    .setState(OutcomeState.FAILURE)
                    .setMessage("All fields must be non-empty.");
        }
        if (repository.findUserById(userId) == null) {
            return OperationOutcome.create()
                    .setState(OutcomeState.FAILURE)
                    .setId(userId)
                    .setMessage("User not exist.");
        }
        if (repository.findArticleById(articleId) == null){
            return OperationOutcome.create()
                    .setState(OutcomeState.FAILURE)
                    .setId(articleId)
                    .setMessage("Article not exist.");
        }
        Notification notification = new Notification(UUID.randomUUID().toString(), userId, articleId, title, content, date);

        repository.saveNotification(notification);
        return OperationOutcome.create()
                .setState(OutcomeState.SUCCESS)
                .setId(notification.getNotificationId())
                .setMessage("Notify user success!");
    }

    public OperationOutcome notifyUsers(List<String> userIds, String articleId, String title, String content, Instant date) {
        for (String userId : userIds) {
            OperationOutcome outcome = notifyUser(userId, articleId, title, content, date);
            if (outcome.getState() == OutcomeState.FAILURE) {
                return outcome;
            }
        }
        return OperationOutcome.create()
                .setState(OutcomeState.SUCCESS)
                .setId(articleId)
                .setMessage("Notify users success!");
    }

    public List<Notification> getUserNotifications(String userId) {
        return repository.findNotificationsByUserId(userId);
    }

    public Notification getNotification(String notificationId) {
        return repository.findNotificationById(notificationId);
    }

    public OperationOutcome removeNotification(String notificationId) {
        repository.deleteNotificationById(notificationId);
        return OperationOutcome.create()
                .setState(OutcomeState.SUCCESS)
                .setId(notificationId)
                .setMessage("remove notification success!");
    }
}
