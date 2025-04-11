package blog.article;

import blog.article.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private Repository repository;

    @BeforeEach
    void setUp() {
        repository.saveArticle(new Article("1", "1", "Original Title", "Original Content", "Original Tag", "Original Category", Instant.now(), false));
    }

    @Test
    public void saveArticle(){
        Article article = new Article();
        article.setUserId("1");
        article.setArticleId("2");
        article.setTitle("save title");
        article.setContent("save content");
        article.setTag("save tag");
        article.setCategory("save category");

        String articleId = articleService.create("1", "2", "save Title", "save Content", "save Tag", "save Category", Instant.now());

        assertEquals("2", articleId);
    }

    @Test
    public void getArticle(){
        Article article = articleService.getArticle("1");

        assertEquals("Original Title", article.getTitle());
        assertEquals("Original Content", article.getContent());
        assertEquals("Original Tag", article.getTag());
        assertEquals("Original Category", article.getCategory());
    }

    @Test
    public void getNotExistArticle(){
        Article article = articleService.getArticle("2");

        assertNull(article);
    }

    @Test
    public void getArticlesByUserId(){
        repository.saveArticle(new Article("1", "2", "Original Title", "Original Content", "Original Tag", "Original Category", Instant.now(), false));

        Collection<Article> articles = articleService.getArticlesByUserId("1");
        for(Article a : articles){
            assertEquals("1", a.getUserId());
        }
    }

    @Test
    public void updateArticle(){
        String articleId = "1";
        String title = "Updated Title";
        String content = "Updated Content";
        String tag = "Updated Tag";
        String category = "Updated Category";

        Boolean success = articleService.update(articleId, title, content, tag, category);

        assertEquals(true, success);

        Article updatedArticle = repository.findArticleById(articleId);
        assertEquals("Updated Title", updatedArticle.getTitle());
        assertEquals("Updated Content", updatedArticle.getContent());
        assertEquals("Updated Tag", updatedArticle.getTag());
        assertEquals("Updated Category", updatedArticle.getCategory());
    }

    @Test
    public void deleteArticle(){
        boolean success = articleService.delete("1", "1");

        assertTrue(success);
    }

    @Test
    public void deleteNotExistArticle(){
        boolean fail = articleService.delete("1", "2");

        assertFalse(fail);
    }

    @Test
    public void recoverArticle(){
        articleService.delete("1", "1");
        boolean success = articleService.recover("1", "1");

        assertTrue(success);
    }

    @Test
    public void recoverNotExistArticle(){
        boolean fail = articleService.recover("1", "1");

        assertFalse(fail);
    }
}
