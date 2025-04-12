package blog.article.collection;

import blog.article.Article;
import blog.article.Repository;
import blog.article.service.ArticleCollectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ArticleCollectionServiceTest {

    @Autowired
    private ArticleCollectionService articleCollectionService;

    @Autowired
    private Repository repository;

    @Test
    public void addArticle(){

        Article article = new Article("1", "1", "test title", "test content", "test tag", "test category", Instant.now(), false);
        repository.saveArticle(article);
        boolean message = articleCollectionService.addArticle("1", article.getArticleId());

        assertTrue(message);
    }

    @Test
    public void deleteArticle(){

        Article article = new Article("1", "1", "test title", "test content", "test tag", "test category", Instant.now(), false);
        repository.saveArticle(article);
        articleCollectionService.addArticle("1", article.getArticleId());
        boolean message = articleCollectionService.deleteArticle("1", article.getArticleId());

        assertTrue(message);
    }
}
