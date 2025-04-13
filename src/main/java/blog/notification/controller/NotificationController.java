package blog.notification.controller;

import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import blog.notification.entity.Notification;
import blog.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public List<Notification> getUserNotifications(@PathVariable("userId") String userId) {
        return notificationService.getUserNotifications(userId);
    }

    @GetMapping("/{notificationId}")
    public Optional<Notification> getNotification(@PathVariable("notificationId") String notificationId) {
        return notificationService.getNotification(notificationId);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable("notificationId") String notificationId) {
        OperationOutcome outcome = notificationService.removeNotification(notificationId);

        if(outcome.getState().equals(OutcomeState.SUCCESS)){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }
}