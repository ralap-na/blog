package blog.feedback.service;

import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FeedbackServiceTest {
    @Autowired
    private FeedbackService feedbackService;

    private final String articleId = "articleId";
    private final String userId = "testerId";
    private final String content = "This is the original content of the comment.";

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
