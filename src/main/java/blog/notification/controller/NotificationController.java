package blog.notification.controller;

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

    @GetMapping
    public List<Notification> getUserNotifications(@RequestBody String userId) {
        return notificationService.getUserNotifications(userId);
    }

    @GetMapping("/{notificationId}")
    public Notification getNotification(@PathVariable String notificationId) {
        return notificationService.getNotification(notificationId);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable String notificationId) {
        boolean message = notificationService.removeNotification(notificationId);

        if (message) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
}