package blog.article;

import blog.article.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private Repository repository;

    @BeforeEach
    void setUp() {
        repository.saveArticle(new Article("1", "1", "Original Title", "Original Content", "oldTag", "oldCategory", Instant.now(), false));
    }

    @Test
    public void updateArticle(){
        String articleId = "1";
        String userId = "1";
        String title = "Updated Title";
        String content = "Updated Content";
        String tag = "tech";
        String category = "blog";

        Boolean success = articleService.update(articleId, title, content, tag, category);

        assertEquals(true, success);

        Article updatedArticle = repository.findArticleById(articleId);
        assertEquals("Updated Title", updatedArticle.getTitle());
        assertEquals("Updated Content", updatedArticle.getContent());
        assertEquals("tech", updatedArticle.getTag());
        assertEquals("blog", updatedArticle.getCategory());
    }
}
