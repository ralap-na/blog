package blog.article;

import blog.chat.Chat;
import blog.notification.entity.Notification;
import blog.feedback.Comment;
import blog.feedback.Reaction;
import blog.user.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Repository {

    private final Map<String, Article> articleList = new HashMap<>();
    private final Map<String, Article> deletedArticleList = new HashMap<>();
    private final Map<String, User> userList = new HashMap<>();
    private final Map<String, Comment> commentList = new HashMap<>();
    private final Map<String, Reaction> reactionList = new HashMap<>();
    private final Map<String, Notification> notificationList = new HashMap<>();
    private final Map<String, Bookmark> bookmarkList = new HashMap<>();
    private final Map<String, Category> categoryList = new HashMap<>();
    private final Map<String, Chat> chatList = new HashMap<>();

    public void clear(){
        articleList.clear();
        deletedArticleList.clear();
    }

    public Collection<Article> findAllArticles(){
        return articleList.values();
    }

    public Collection<Article> findAllDeletedArticlesByUserId(String userId){
        Collection<Article> articles = deletedArticleList.values();
        articles = articles.stream().filter(article -> userId.equals(article.getUserId())).toList();
        return articles;
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

    // Comment
    public void clearFeedback(){
        commentList.clear();
        reactionList.clear();
    }

    public Comment findCommentById(String commentId){
        return commentList.get(commentId);
    }

    public List<Comment> findCommentsByArticleId(String articleId){
        return commentList.values().stream()
                .filter(comment -> comment.getArticleId().equals(articleId))
                .toList();
    }

    public List<Comment> findAllComments(){
        return commentList.values().stream().toList();
    }

    public void saveComment(Comment comment) {
        commentList.put(comment.getId(), comment);
    }

    public void deleteComment(String commentId) {
        commentList.remove(commentId);
    }

    // Reaction

    public List<Reaction> findReactionsByArticleId(String articleId){
        return reactionList.values().stream()
                .filter(reaction -> reaction.getArticleId().equals(articleId))
                .toList();
    }

    public List<Reaction> findReactionsByCommentId(String articleId, String commentId){
        return reactionList.values().stream()
                .filter(reaction -> reaction.getArticleId().equals(articleId) && reaction.getCommentId().equals(commentId))
                .toList();
    }

    public Reaction findReactionById(String reactionId){
        return reactionList.get(reactionId);
    }

    public List<Reaction> findAllReactions(){
        return reactionList.values().stream().toList();
    }

    public void saveReaction(Reaction reaction) {
        reactionList.put(reaction.getId(), reaction);
    }

    public void deleteReaction(String reactionId) {
        reactionList.remove(reactionId);
    }

    // User

    public Optional<User> findUserByUsername(String username) {
        return userList.values().stream().filter(user -> user.getUsername().equals(username)).findAny();
    }

    public void saveUser(User user) {
        userList.put(user.getUserId(), user);
    }

    public Map<String, String> findUsernamesByUserId(List<String> userIds) {
        Map<String, String> result = new HashMap<>();
        for (String id : userIds) {
            if (userList.containsKey(id)) {
                result.put(id, userList.get(id).getUsername());
            }
        }
        return result;
    }

    public User findUserById(String userId) {
        return userList.get(userId);
    }

    public void deleteUser(String userId) {
        userList.remove(userId);
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

    @PostConstruct
    public void initDefaultCategories() {
        for (DefaultCategory dc : DefaultCategory.values()) {
            String id = UUID.randomUUID().toString();
            Category category = new Category(id, dc.name());
            categoryList.put(id, category);
        }
    }

    public void saveCategory(Category category) {
        categoryList.put(category.getId(), category);
    }

    public Category findCategoryById(String id) {
        return categoryList.get(id);
    }

    public Collection<Category> findAllCategories() {
        return categoryList.values();
    }

    public boolean deleteCategoryById(String id) {
        return categoryList.remove(id) != null;
    }

    public void saveChat(Chat chat) {
        chatList.put(chat.getId(), chat);
    }

    public Chat findChat(String chatId) {
        return chatList.get(chatId);
    }

    public Map<String, Chat> findChatByUserId(String userId) {
        return chatList.values().stream()
                .filter(chat -> chat.getUser1Id().equals(userId) || chat.getUser2Id().equals(userId))
                .collect(Collectors.toMap(
                        chat -> {
                            return chat.getUser1Id().equals(userId) ? chat.getUser2Id() : chat.getUser1Id();
                        },
                        chat -> chat
                ));
    }


    public boolean findChatsByUsers(String user1Id, String user2Id) {
        for (Chat chat : chatList.values()) {
            if (chat.getUser1Id().equals(user1Id) && chat.getUser2Id().equals(user2Id)) {
                return true;
            }
            if (chat.getUser1Id().equals(user2Id) && chat.getUser2Id().equals(user1Id)) {
                return true;
            }
        }
        return false;
    }
}