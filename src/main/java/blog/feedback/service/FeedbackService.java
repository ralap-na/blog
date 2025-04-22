package blog.feedback.service;

import blog.article.Repository;
import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import blog.feedback.Comment;
import blog.feedback.Reaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {
    @Autowired
    private final Repository repository;

    public FeedbackService(Repository repository) {
        this.repository = repository;
    }

    public OperationOutcome createComment(String articleId, String userId, String content, Instant date) {
        if (content.isEmpty()) {
            return OperationOutcome.create().setId(articleId).setMessage("Content should not be empty.").setState(OutcomeState.FAILURE);
        }

        String commentId = UUID.randomUUID().toString();
        Comment comment = new Comment(commentId, articleId, userId, content, date);
        repository.saveComment(comment);
        return OperationOutcome.create().setId(commentId).setState(OutcomeState.SUCCESS);
    }

    public OperationOutcome deleteComment(String commentId, String userId) {
        Comment comment = repository.findCommentById(commentId);

        if (comment == null) {
            return OperationOutcome.create().setId(commentId).setMessage("Comment not found.").setState(OutcomeState.FAILURE);
        }

        if (!comment.getUserId().equals(userId)) {
            return OperationOutcome.create().setId(commentId).setMessage("Invalid user.").setState(OutcomeState.FAILURE);
        }

        repository.deleteComment(commentId);
        return OperationOutcome.create().setId(commentId).setState(OutcomeState.SUCCESS);
    }

    public List<Comment> getCommentsByArticleId(String articleId) {
        return repository.findCommentsByArticleId(articleId);
    }

    public List<Comment> getAllComments() {
        return repository.findAllComments();
    }

    public Comment getCommentById(String commentId) {
        return repository.findCommentById(commentId);
    }

    public OperationOutcome addReactionOnArticle(String articleId, String userId, String type) {
        String reactionId = UUID.randomUUID().toString();
        Reaction reaction = new Reaction(reactionId, userId, articleId, type);
        repository.saveReaction(reaction);
        return OperationOutcome.create().setId(reactionId).setState(OutcomeState.SUCCESS);
    }

    public OperationOutcome addReactionOnComment(String articleId, String commentId, String userId, String type) {
        String reactionId = UUID.randomUUID().toString();
        Reaction reaction = new Reaction(reactionId, userId, articleId, commentId, type);
        repository.saveReaction(reaction);
        return OperationOutcome.create().setId(reactionId).setState(OutcomeState.SUCCESS);
    }

    public OperationOutcome removeReaction(String reactionId, String userId) {
        Reaction reaction = repository.findReactionById(reactionId);

        if (reaction == null) {
            return OperationOutcome.create().setId(reactionId).setMessage("Fail to find reaction.").setState(OutcomeState.FAILURE);
        }

        if (!userId.equals(reaction.getUserId())) {
            return OperationOutcome.create().setId(reactionId).setMessage("Invalid user.").setState(OutcomeState.FAILURE);
        }

        repository.deleteReaction(reactionId);
        return OperationOutcome.create().setId(reactionId).setState(OutcomeState.SUCCESS);
    }

    public List<Reaction> getReactionsByArticleId(String articleId) {
        return repository.findReactionsByArticleId(articleId);
    }

    public List<Reaction> getReactionsByCommentId(String articleId, String commentId) {
        return repository.findReactionsByCommentId(articleId, commentId);
    }

    public List<Reaction> getAllReactions() {
        return repository.findAllReactions();
    }

    public Reaction getReactionById(String reactionId) {
        return repository.findReactionById(reactionId);
    }
}
