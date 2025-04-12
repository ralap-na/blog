package blog.article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleTest {

    private Article article;

    @BeforeEach
    public void setUp() {
        Instant fixedTime = Instant.parse("2024-01-01T00:00:00Z");
        article = new Article("1", "1", "test title", "test content", "test tag", "test category", fixedTime, false);
    }

    @Test
    public void updateArticle(){
        article.update("Updated Title", "Updated Content", "Updated Tag", "Updated Category");

        assertEquals("Updated Title", article.getTitle());
        assertEquals("Updated Content", article.getContent());
        assertEquals("Updated Tag", article.getTag());
        assertEquals("Updated Category", article.getCategory());
    }

    @Test
    public void deleteArticle(){
        article.delete();

        assertTrue(article.getDeleted());
    }

    @Test
    public void recoverArticle(){
        article.delete();
        article.recover();

        assertFalse(article.getDeleted());
    }
}
