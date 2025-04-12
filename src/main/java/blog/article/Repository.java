package blog.article;

import blog.notification.entity.Notification;
import blog.feedback.Comment;
import blog.feedback.Reaction;
import blog.user.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class Repository {

    private final Map<String, Article> articleList = new HashMap<>();
    private final Map<String, Article> deletedArticleList = new HashMap<>();
    private final Map<String, User> userList = new HashMap<>();
    private final Map<String, Comment> CommentList = new HashMap<>();
    private final Map<String, Reaction> ReactionList = new HashMap<>();
    private final Map<String, Notification> notificationList = new HashMap<>();
    private final Map<String, Bookmark> bookmarkList = new HashMap<>();


    public void clear(){
        articleList.clear();
    }

    public Collection<Article> findAllArticles(){
        return articleList.values();
    }

    public Article findArticleById(String articleId){
        return articleList.get(articleId);
    }

    public Collection<Article> findArticlesByUserId(String userId){
        Collection<Article> articles = articleList.values();
        articles = articles.stream().filter(article -> userId.equals(article.getUserId())).toList();
        return articles;
    }

    public Collection<Article> findArticlesByTitle(String keyword){
        Collection<Article> articles = articleList.values();
        articles = articles.stream().filter(article -> article.getTitle().contains(keyword)).toList();
        return articles;
    }

    public Collection<Article> findArticlesByTag(String tag){
        Collection<Article> articles = articleList.values();
        articles = articles.stream().filter(article -> article.getTag().contains(tag)).toList();
        return articles;
    }

    public Collection<Article> findArticlesByCategory(String category){
        Collection<Article> articles = articleList.values();
        articles = articles.stream().filter(article -> article.getCategory().equals(category)).toList();
        return articles;
    }

    public void saveArticle(Article article) {
        articleList.put(article.getArticleId(), article);
    }
  
    public Article findDeletedArticleById(String articleId){
        return deletedArticleList.get(articleId);
    }

    public void delete(String articleId) {
        Article article = articleList.get(articleId);
        article.delete();
        articleList.remove(articleId);

        deletedArticleList.put(articleId, article);
    }

    public void recover(String articleId){
        Article article = deletedArticleList.get(articleId);
        article.recover();
        deletedArticleList.remove(articleId);

        articleList.put(articleId, article);
    }

    public Comment findCommentById(String commentId){
        return CommentList.get(commentId);
    }

    public Reaction findReactionById(String reactionId){
        return ReactionList.get(reactionId);
    }

    public void saveComment(Comment comment) {
        CommentList.put(comment.getId(), comment);
    }

    public void saveReaction(Reaction reaction) {
        ReactionList.put(reaction.getId(), reaction);
    }

    public void deleteReaction(String reactionId) {
        ReactionList.remove(reactionId);
    }
      
    public Optional<User> findUserByUsername(String username) {
        return userList.values().stream().filter(user -> user.getUsername().equals(username)).findAny();
    }

    public void saveUser(User user) {
        userList.put(user.getUserId(), user);
    }

    public User findUserById(String userId) {
        return userList.get(userId);
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
  
    public void saveBookmark(Bookmark bookmark) {
        bookmarkList.put(bookmark.getUserId(), bookmark);
    }

    public Bookmark findBookmarkByUserId(String userId){
        return bookmarkList.get(userId);
    }
}