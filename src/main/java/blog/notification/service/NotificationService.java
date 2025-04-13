package blog.notification.service;

import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import blog.notification.entity.Notification;
import blog.notification.repository.NotificationRepository;
import blog.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public OperationOutcome notifyUser(String userId, String title, String content, Instant date) {
        if (userRepository.findById(userId).isEmpty()) {
            return OperationOutcome.create().setMessage("Invalid user Id: " + userId).setState(OutcomeState.FAILURE);
        }
        if (title == null || content == null || date == null) {
            return OperationOutcome.create().setId("").setMessage("All notification fields must be non-empty.").setState(OutcomeState.FAILURE);
        }

        String notificationId = UUID.randomUUID().toString();
        Notification notification = new Notification(notificationId, userId, title, content, date);

        notificationRepository.save(notification);
        return OperationOutcome.create().setId(notificationId).setState(OutcomeState.SUCCESS);
    }

    public OperationOutcome notifyUsers(List<String> userIds, String title, String content, Instant date) {
        for (String userId : userIds) {
            OperationOutcome outcome = notifyUser(userId, title, content, date);
            if (outcome.getState() != OutcomeState.SUCCESS) {
                return outcome;
            }
        }
        return OperationOutcome.create().setState(OutcomeState.SUCCESS);
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findNotificationsByUserId(userId);
    }

    public Optional<Notification> getNotification(String notificationId) {
        return notificationRepository.findById(notificationId);
    }

    public OperationOutcome removeNotification(String notificationId) {
        if (notificationRepository.findById(notificationId).isEmpty()) {
            return OperationOutcome.create().setId(notificationId).setMessage("Notification not found.").setState(OutcomeState.FAILURE);
        }

        notificationRepository.deleteById(notificationId);
        return OperationOutcome.create().setState(OutcomeState.SUCCESS);
    }
}