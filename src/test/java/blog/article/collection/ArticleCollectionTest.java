package blog.article.collection;

import blog.article.Article;
import blog.article.ArticleCollection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArticleCollectionTest {

    @Test
    public void addArticle() {
        Article article1 = new Article();
        article1.setUserId("u1");
        article1.setArticleId("a1");

        Article article2 = new Article();
        article2.setUserId("u2");
        article2.setArticleId("a2");

        ArticleCollection articleCollection = new ArticleCollection("c1", "u3");
        articleCollection.addArticle(article1.getArticleId());
        articleCollection.addArticle(article2.getArticleId());

        List<String> articleIds = articleCollection.getArticleIds();
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

        ArticleCollection articleCollection = new ArticleCollection("c1", "u3");
        articleCollection.addArticle(article1.getArticleId());
        articleCollection.addArticle(article2.getArticleId());

        articleCollection.deleteArticle(article1.getArticleId());

        List<String> articleIds = articleCollection.getArticleIds();
        assertFalse(articleIds.contains("a1"));
        assertTrue(articleIds.contains("a2"));
    }
}
