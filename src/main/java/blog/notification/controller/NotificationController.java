package blog.notification.controller;

import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import blog.notification.entity.Notification;
import blog.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable("userId") String userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);

        if (notifications != null) {
            return ResponseEntity.ok(notifications);
        } else {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<Notification> getNotification(@PathVariable("notificationId") String notificationId) {
        Notification notification =  notificationService.getNotification(notificationId);
        if (notification != null) {
            return ResponseEntity.ok(notification);
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable("notificationId") String notificationId) {
        OperationOutcome outcome = notificationService.removeNotification(notificationId);

        if (outcome.getState() == OutcomeState.SUCCESS) {
            return ResponseEntity.ok("Deleted notification successfully");
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
}