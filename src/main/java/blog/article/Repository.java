package blog.article;


import blog.feedback.Comment;
import blog.feedback.Reaction;
import blog.user.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class Repository {

    private final Map<String, Article> articleList = new HashMap<>();
    private final Map<String, User> userList = new HashMap<>();
    private final Map<String, Comment> CommentList = new HashMap<>();
    private final Map<String, Reaction> ReactionList = new HashMap<>();

    public Article findArticleById(String articleId){
        return articleList.get(articleId);
    }

    public void saveArticle(Article article) {
        articleList.put(article.getArticleId(), article);
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
