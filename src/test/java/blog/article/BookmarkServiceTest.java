package blog.article;

import blog.article.service.BookmarkService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookmarkServiceTest {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private Repository repository;

    @AfterEach
    public void tearDown() {
        // 清空 Repository 裡的資料
        repository.clear();
    }

    @Test
    public void addArticle(){
        Article article = new Article();
        article.setArticleId("a1");
        article.setUserId("u1");
        repository.saveArticle(article);
        boolean message = bookmarkService.addArticle("u2", article.getArticleId());

        assertTrue(message);
    }

    @Test
    public void deleteArticle(){
        Article article = new Article();
        article.setArticleId("a1");
        article.setUserId("u1");
        repository.saveArticle(article);
        bookmarkService.addArticle("u2", article.getArticleId());
        boolean message = bookmarkService.deleteArticle("u2", article.getArticleId());

        assertTrue(message);
    }

    @Test
    public void getArticleIds(){
        Article article1 = new Article();
        article1.setArticleId("a1");
        article1.setUserId("u1");

        Article article2 = new Article();
        article2.setArticleId("a2");
        article2.setUserId("u1");

        repository.saveArticle(article1);
        repository.saveArticle(article2);
        bookmarkService.addArticle("u2", article1.getArticleId());
        bookmarkService.addArticle("u2", article2.getArticleId());

        List<String> articleIds = bookmarkService.getArticleIds("u2");

        assertTrue(articleIds.contains("a1"));
        assertTrue(articleIds.contains("a2"));
        assertEquals(2, articleIds.size());
    }
}
