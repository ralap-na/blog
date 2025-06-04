package blog.feedback.service;

import blog.article.Repository;
import blog.article.service.ArticleService;
import blog.common.OperationOutcome;
import blog.common.OutcomeState;
import blog.user.service.UserService;
import blog.feedback.Comment;
import blog.feedback.Reaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FeedbackServiceTest {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private Repository repository;
    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    private final String articleId = "articleId";
    private String testerId;
    private String articleOwnerId;
    private final String content = "This is the original content of the comment.";

    @BeforeEach
    public void setUp() {
        repository.clear();

        userService.createUser("Tester", "Tester");
        userService.createUser("ArticleOwner", "ArticleOwner");

        testerId = repository.findUserByUsername("Tester").get().getUserId();
        articleOwnerId = repository.findUserByUsername("ArticleOwner").get().getUserId();

        articleService.create(articleOwnerId, articleId, "Article Title", "Article Content", "Tag", "Category", Instant.now());

        repository.clearFeedback();
        articleService.create("articleOwner", articleId, "Article Title", "Article Content", "Tag", "Category", Instant.now());
    }

    @AfterEach
    public void tearDown() {
        repository.deleteUser(testerId);
        repository.deleteUser(articleOwnerId);
        repository.clear();
        repository.clearFeedback();
    }

    @Test
    public void create_a_comment() {
        OperationOutcome outcome = feedbackService.createComment(articleId, testerId, content, Instant.now());
        assertEquals(OutcomeState.SUCCESS, outcome.getState());
    }

    @Test
    public void create_a_comment_with_empty_content_should_fail() {
        OperationOutcome outcome = feedbackService.createComment(articleId, testerId, "", Instant.now());
        assertEquals(OutcomeState.FAILURE, outcome.getState());
    }
  
    @Test
    public void delete_a_comment() {
        OperationOutcome createOutcome = feedbackService.createComment(articleId, testerId, content, Instant.now());
        String commentId = createOutcome.getId();

        OperationOutcome deleteOutcome = feedbackService.deleteComment(commentId, testerId);
        assertEquals(OutcomeState.SUCCESS, deleteOutcome.getState());
    }

    @Test
    public void article_owner_delete_a_comment_should_success() {
        OperationOutcome createOutcome = feedbackService.createComment(articleId, testerId, content, Instant.now());
        String commentId = createOutcome.getId();

        OperationOutcome deleteOutcome = feedbackService.deleteComment(commentId, articleOwnerId);
        assertEquals(OutcomeState.SUCCESS, deleteOutcome.getState());
    }

    @Test
    public void delete_a_comment_with_invalid_comment_id_should_fail() {
        OperationOutcome deleteOutcome = feedbackService.deleteComment("invalidCommentId", testerId);
        assertEquals(OutcomeState.FAILURE, deleteOutcome.getState());
    }

    @Test
    public void delete_a_comment_with_invalid_user_should_fail() {
        OperationOutcome createOutcome = feedbackService.createComment(articleId, testerId, content, Instant.now());
        String commentId = createOutcome.getId();

        OperationOutcome deleteOutcome = feedbackService.deleteComment(commentId, "invalidUserId");
        assertEquals(OutcomeState.FAILURE, deleteOutcome.getState());
    }

    @Test
    public void get_comments_by_article_id() {
        feedbackService.createComment(articleId, testerId, content, Instant.now());
        feedbackService.createComment(articleId, testerId, "Another comment", Instant.now());

        var comments = feedbackService.getCommentsByArticleId(articleId);
        assertEquals(2, comments.size());
    }

    @Test
    public void get_all_comments() {
        feedbackService.createComment(articleId, testerId, content, Instant.now());
        feedbackService.createComment(articleId, testerId, "Another comment", Instant.now());

        var comments = feedbackService.getAllComments();
        assertEquals(2, comments.size());
    }

    @Test
    public void get_comment_by_id() {
        OperationOutcome createOutcome = feedbackService.createComment(articleId, testerId, content, Instant.now());
        String commentId = createOutcome.getId();

        var comment = feedbackService.getCommentById(commentId);
        assertEquals(commentId, comment.getId());
    }

    @Test
    public void add_reaction_on_article() {
        OperationOutcome outcome = feedbackService.addReactionOnArticle(articleId, testerId, "like");
        assertEquals(OutcomeState.SUCCESS, outcome.getState());
    }

    @Test
    public void add_reaction_on_comment() {
        OperationOutcome createOutcome = feedbackService.createComment(articleId, testerId, content, Instant.now());
        String commentId = createOutcome.getId();

        OperationOutcome outcome = feedbackService.addReaction(articleId, commentId, userId, "like");
      
        assertEquals(OutcomeState.SUCCESS, outcome.getState());
    }
}
