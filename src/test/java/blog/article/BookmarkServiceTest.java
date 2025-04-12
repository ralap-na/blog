package blog.article;

import blog.article.service.BookmarkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookmarkServiceTest {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private Repository repository;

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
}
