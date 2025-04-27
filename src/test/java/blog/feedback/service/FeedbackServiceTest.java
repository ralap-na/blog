package blog.feedback.service;

import blog.article.Repository;
import blog.article.service.ArticleService;
import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FeedbackServiceTest {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private Repository repository;

    private final String articleId = "articleId";
    private final String userId = "testerId";
    private final String content = "This is the original content of the comment.";

    @BeforeEach
    public void setUp() {
        repository.clear();
        articleService.create("articleOwner", articleId, "Article Title", "Article Content", "Tag", "Category", Instant.now());
    }

    @AfterEach
    public void tearDown() {
        repository.clear();
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
    public void delete_a_comment() {
        OperationOutcome createOutcome = feedbackService.createComment(articleId, userId, content, Instant.now());
        String commentId = createOutcome.getId();

        OperationOutcome deleteOutcome = feedbackService.deleteComment(commentId, userId);
        assertEquals(OutcomeState.SUCCESS, deleteOutcome.getState());
    }

    @Test
    public void article_owner_delete_a_comment_should_success() {
        OperationOutcome createOutcome = feedbackService.createComment(articleId, userId, content, Instant.now());
        String commentId = createOutcome.getId();

        OperationOutcome deleteOutcome = feedbackService.deleteComment(commentId, "articleOwner");
        assertEquals(OutcomeState.SUCCESS, deleteOutcome.getState());
    }

    @Test
    public void delete_a_comment_with_invalid_comment_id_should_fail() {
        OperationOutcome deleteOutcome = feedbackService.deleteComment("invalidCommentId", userId);
        assertEquals(OutcomeState.FAILURE, deleteOutcome.getState());
    }

    @Test
    public void delete_a_comment_with_invalid_user_should_fail() {
        OperationOutcome createOutcome = feedbackService.createComment(articleId, userId, content, Instant.now());
        String commentId = createOutcome.getId();

        OperationOutcome deleteOutcome = feedbackService.deleteComment(commentId, "invalidUserId");
        assertEquals(OutcomeState.FAILURE, deleteOutcome.getState());
    }

    @Test
    public void get_comments_by_article_id() {
        feedbackService.createComment(articleId, userId, content, Instant.now());
        feedbackService.createComment(articleId, userId, "Another comment", Instant.now());

        var comments = feedbackService.getCommentsByArticleId(articleId);
        assertEquals(2, comments.size());
    }

    @Test
    public void get_all_comments() {
        feedbackService.createComment(articleId, userId, content, Instant.now());
        feedbackService.createComment(articleId, userId, "Another comment", Instant.now());

        var comments = feedbackService.getAllComments();
        assertEquals(2, comments.size());
    }

    @Test
    public void get_comment_by_id() {
        OperationOutcome createOutcome = feedbackService.createComment(articleId, userId, content, Instant.now());
        String commentId = createOutcome.getId();

        var comment = feedbackService.getCommentById(commentId);
        assertEquals(commentId, comment.getId());
    }

    @Test
    public void add_reaction_on_article() {
        OperationOutcome outcome = feedbackService.addReactionOnArticle(articleId, userId, "like");
        assertEquals(OutcomeState.SUCCESS, outcome.getState());
    }

    @Test
    public void add_reaction_on_comment() {
        OperationOutcome createOutcome = feedbackService.createComment(articleId, userId, content, Instant.now());
        String commentId = createOutcome.getId();

        OperationOutcome outcome = feedbackService.addReactionOnComment(articleId, commentId, userId, "like");
        assertEquals(OutcomeState.SUCCESS, outcome.getState());
    }
}
