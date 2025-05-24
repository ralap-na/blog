package blog.article;

import blog.article.service.ArticleService;
import blog.user.User;
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

    Instant fixedTime = Instant.parse("2024-01-01T00:00:00Z");

    @BeforeEach
    void setUp() {
        repository.clear();
        Article article = new Article();
        article.setUserId("1");
        article.setArticleId("1");
        article.setTitle("Original Title");
        article.setContent("Original Content");
        article.setTag("Original Tag");
        article.setCategory("Original Category");
        article.setDate(fixedTime);
        article.setDeleted(false);
        repository.saveArticle(article);
    }

    @Test
    public void saveArticle(){

        String articleId = articleService.create("1", "2", "Saved Title", "Saved Content", "Saved Tag", "Saved Category", fixedTime);

        assertEquals("2", articleId);
    }

    @Test
    public void saveArticle2(){
        User user = repository.findUserByUsername("Admin").get();

        String articleId = articleService.createV2(user, "2", "Saved Title", "Saved Content", "Saved Tag", "Saved Category", fixedTime);

        assertEquals("2", articleId);

        user.clear();
    }

    @Test
    public void getAllArticles(){
        repository.saveArticle(new Article("2", "2", "Other Title", "Other Content", "Other Tag", "Other Category", fixedTime, false));

        Collection<Article> articles = articleService.getAllArticles();

        assertEquals(2, articles.size());
    }

    @Test
    public void getAllDeletedArticlesByUserId(){
        repository.saveArticle(new Article("2", "2", "Other Title", "Other Content", "Other Tag", "Other Category", fixedTime, false));

        repository.delete("1");
        repository.delete("2");

        Collection<Article> articles = articleService.getAllDeletedArticlesByUserId("2");

        assertEquals(1, articles.size());
    }

    @Test
    public void getArticlesByConditions(){
        repository.saveArticle(new Article("2", "2", "Other Title", "Other Content", "Other Tag", "Other Category", fixedTime, false));


        Collection<Article> articles = articleService.getArticlesByConditions("Other Title", "Other Category", "Other Tag");

        assertEquals(1, articles.size());

        Article article = articles.stream().toList().get(0);
        assertEquals("Other Title", article.getTitle());
        assertEquals("Other Tag", article.getTag());
        assertEquals("Other Category", article.getCategory());
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
        Article article = articleService.getArticle("3");

        assertNull(article);
    }

    @Test
    public void getArticlesByUserId(){
        repository.saveArticle(new Article("2", "2", "Original Title", "Original Content", "Original Tag", "Original Category", fixedTime, false));

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
