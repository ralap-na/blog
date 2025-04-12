package blog.feedback;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

public class CommentTest {
    private String articleId = UUID.randomUUID().toString();
    private String userId = "tester";
    private String content = "This is the original content of this comment.";
    private Instant date = Instant.now();


    @Test
    public void create_a_comment(){
        Comment comment = new Comment(UUID.randomUUID().toString(), articleId, userId, content, date);

        Assertions.assertEquals(articleId, comment.getArticleId());
        Assertions.assertEquals(userId, comment.getUserId());
        Assertions.assertEquals(content, comment.getContent());
        Assertions.assertEquals(date, comment.getDate());
    }

    @Test
    public void update_a_comment(){
        Instant newDate = Instant.now();
        String newContent = "This is the new content of this comment.";

        Comment comment = new Comment(UUID.randomUUID().toString(), articleId, userId, content, date);

        comment.update(newContent, newDate);

        Assertions.assertEquals(newContent, comment.getContent());
        Assertions.assertEquals(date, comment.getDate());
    }
}
