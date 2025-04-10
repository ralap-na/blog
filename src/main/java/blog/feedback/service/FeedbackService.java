package blog.feedback.service;

import blog.article.Repository;
import blog.feedback.Comment;
import blog.feedback.Reaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class FeedbackService {
    @Autowired
    private Repository repository;

    public Boolean createComment(String articleId, String userId, String content, Instant date) {
        if (content.isEmpty()) {
            return false;
        }

        String commentId = UUID.randomUUID().toString();
        Comment comment = new Comment(commentId, articleId, userId, content, date);
        repository.saveComment(comment);
        return true;
    }

    public Boolean updateComment(String id, String userId, String content, Instant date) {
        Comment comment = repository.findCommentById(id);

        if(comment == null) {
            return false;
        }

        if (!userId.equals(comment.getUserId())) {
            return false;
        }

        if (content.equals(comment.getContent())) {
            return true;
        }

        if(date.isBefore(comment.getDate())) {
            return false;
        }

        comment.update(content, date);
        repository.saveComment(comment);

        return true;
    }

    public Boolean addReaction(String articleId, String userId, String type) {
        String reactionId = UUID.randomUUID().toString();
        Reaction reaction = new Reaction(reactionId, articleId, userId, type);
        repository.saveReaction(reaction);
        return true;
    }

    public Boolean removeReaction(String reactionId, String userId) {
        Reaction reaction = repository.findReactionById(reactionId);

        if (reaction == null) {
            return false;
        }

        if (!userId.equals(reaction.getUserId())) {
            return false;
        }

        repository.deleteReaction(reactionId);
        return true;
    }
}
