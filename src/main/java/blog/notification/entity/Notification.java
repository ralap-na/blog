package blog.notification.entity;

import java.time.Instant;

public class Notification {
    private String notificationId;
    private String userId;
    private String title;
    private String content;
    private Instant date;

    public Notification(String notificationId, String userId, String title, String content, Instant date) {
        if (notificationId == null || userId == null || title == null || content == null || date == null) {
            throw new IllegalArgumentException("All fields must be non-null");
        }
        if (notificationId.isEmpty() || userId.isEmpty() || title.isEmpty() || content.isEmpty()) {
            throw new IllegalArgumentException("All fields must be non-empty");
        }
        if (date.isAfter(Instant.now())) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }

        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}


