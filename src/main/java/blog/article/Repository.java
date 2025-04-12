package blog.article;


import blog.feedback.Comment;
import blog.feedback.Reaction;
import blog.user.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class Repository {

    private final Map<String, Article> articleList = new HashMap<>();
    private final Map<String, Article> deletedArticleList = new HashMap<>();
    private final Map<String, User> userList = new HashMap<>();
    private final Map<String, Comment> CommentList = new HashMap<>();
    private final Map<String, Reaction> ReactionList = new HashMap<>();


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
        return userList.values().stream().filter(user -> user.getUserId().equals(username)).findAny();
    }

    public void saveUser(User user) {
        userList.put(user.getUserId(), user);
    }

    public User findUserById(String userId) {
        return userList.get(userId);
    }
}