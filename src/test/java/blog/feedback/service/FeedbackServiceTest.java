package blog.feedback.service;

import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class FeedbackServiceTest {
    private FeedbackService feedbackService;

    private String articleId = "articleId";
    private String userId = "testerId";
    private String content = "This is the original content of the comment.";

    @BeforeEach
    public void setUp() {
        feedbackService = new FeedbackService();
    }

    @Test
    public void create_a_comment() {
        OperationOutcome outcome = feedbackService.createComment(articleId, userId, content, Instant.now());
        assertEquals(OutcomeState.SUCCESS, outcome.getState());
    }

    @Test
    public void create_a_comment_with_empty_content_should_fail() {
        OperationOutcome outcome = feedbackService.createComment(articleId, userId, "", Instant.now());
        assertEquals(OutcomeState.FAILURE, outcome.getState());
    }

    @Test
    public void update_a_comment() {
        String commentId = feedbackService.createComment(articleId, userId, content, Instant.now()).getId();
        OperationOutcome outcome = feedbackService.updateComment(commentId, userId, content, Instant.now());
        assertEquals(OutcomeState.SUCCESS, outcome.getState());
    }
}
