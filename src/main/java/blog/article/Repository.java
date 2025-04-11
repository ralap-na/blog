package blog.article;

import blog.feedback.Comment;
import blog.feedback.Reaction;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Repository {

    private Map<String, Article> articleList = new HashMap<>();
    private Map<String, Comment> CommentList = new HashMap<>();
    private Map<String, Reaction> ReactionList = new HashMap<>();

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
}
