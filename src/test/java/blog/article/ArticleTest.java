package blog.article;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class ArticleTest {

    @Test
    public void updateArticle(){
        Article article = new Article("1", "1", "test title", "test content", "test tag", "test category", Instant.now(), false);

        article.update("update title", "update content", "update tag", "update category");

        Assertions.assertEquals(article.getTitle(), "update title");
        Assertions.assertEquals(article.getContent(), "update content");
        Assertions.assertEquals(article.getTag(), "update tag");
        Assertions.assertEquals(article.getCategory(), "update category");
    }
}
