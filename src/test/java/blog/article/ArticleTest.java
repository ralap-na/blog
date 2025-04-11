package blog.article;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArticleTest {

    @Test
    public void updateArticle(){
        Article article = new Article("1", "1", "test title", "test content", "test tag", "test category", Instant.now(), false);

        article.update("Updated Title", "Updated Content", "Updated Tag", "Updated Category");

        assertEquals("Updated Title", article.getTitle());
        assertEquals("Updated Content", article.getContent());
        assertEquals("Updated Tag", article.getTag());
        assertEquals("Updated Category", article.getCategory());
    }

    @Test
    public void deleteArticle(){
        Article article = new Article("1", "1", "test title", "test content", "test tag", "test category", Instant.now(), false);

        article.delete();

        Assertions.assertEquals(true, article.getDeleted());
    }
}
