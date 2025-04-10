package blog.article;

import blog.notification.entity.Notification;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Repository {

    private Map<String, Article> articleList = new HashMap<>();
    private Map<String, Notification> notificationList = new HashMap<>();

    public Article findArticleById(String articleId){
        return articleList.get(articleId);
    }

    public void saveArticle(Article article) {
        articleList.put(article.getArticleId(), article);
    }

    public Notification findNotificationById(String id) {
        return notificationList.get(id);
    }

    public List<Notification> findNotificationsByUserId(String userId) {
        return notificationList.values().stream()
                .filter(notification -> notification.getUserId().equals(userId))
                .toList();
    }

    public void saveNotification(Notification notification) {
        notificationList.put(notification.getNotificationId(), notification);
    }

    public void deleteNotificationById(String notificationId) {
        notificationList.remove(notificationId);
    }
}
