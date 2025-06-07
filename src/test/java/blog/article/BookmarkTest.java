package blog.article;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookmarkTest {

    @Test
    public void addArticle() {
        Article article1 = new Article();
        article1.setUserId("u1");
        article1.setArticleId("a1");

        Article article2 = new Article();
        article2.setUserId("u2");
        article2.setArticleId("a2");

        Bookmark bookmark = new Bookmark("b1", "Bookmark-1");
        bookmark.addArticle(article1.getArticleId());
        bookmark.addArticle(article2.getArticleId());

        List<String> articleIds = bookmark.getArticleIds();
        assertTrue(articleIds.contains("a1"));
        assertTrue(articleIds.contains("a2"));
    }

    @Test
    public void deleteArticle() {
        Article article1 = new Article();
        article1.setUserId("u1");
        article1.setArticleId("a1");

        Article article2 = new Article();
        article2.setUserId("u2");
        article2.setArticleId("a2");

        Bookmark bookmark = new Bookmark("b1", "Bookmark-1");
        bookmark.addArticle(article1.getArticleId());
        bookmark.addArticle(article2.getArticleId());

        bookmark.deleteArticle(article1.getArticleId());

        List<String> articleIds = bookmark.getArticleIds();
        assertFalse(articleIds.contains("a1"));
        assertTrue(articleIds.contains("a2"));
    }
}
