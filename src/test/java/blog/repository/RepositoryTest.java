package blog.repository;

import blog.article.Article;
import blog.article.Bookmark;
import blog.article.Repository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class RepositoryTest {

    private final Repository repository = new Repository();

    @Test
    public void saveArticle(){
        Article article = buildTestArticle("1", "Saved", false);

        repository.saveArticle(article);

        Article savedArticle = repository.findArticleById("1");

        assertEquals("Saved Title", savedArticle.getTitle());
        assertEquals("Saved Content", savedArticle.getContent());
        assertEquals("Saved Tag", savedArticle.getTag());
        assertEquals("Saved Category", savedArticle.getCategory());
    }

    @Test
    public void findAllArticle(){
        Article article = buildTestArticle("1", "A", false);
        Article article2 = buildTestArticle("2", "B", false);

        repository.saveArticle(article);
        repository.saveArticle(article2);

        Collection<Article> articles = repository.findAllArticles();

        assertEquals(2, articles.size());
    }

    @Test
    public void findArticlesByTag(){
        Article article = buildTestArticle("1", "A", false);
        Article article2 = buildTestArticle("2", "B", false);

        repository.saveArticle(article);
        repository.saveArticle(article2);

        Collection<Article> articles = repository.findArticlesByTag("B");

        for(Article a : articles){
            assertEquals("B Tag", a.getTag());
        }
    }

    @Test
    public void findArticlesByCategory(){
        Article article = buildTestArticle("1", "A", false);
        Article article2 = buildTestArticle("2", "B", false);

        repository.saveArticle(article);
        repository.saveArticle(article2);

        Collection<Article> articles = repository.findArticlesByCategory("B Category");

        for(Article a : articles){
            assertEquals("B Category", a.getCategory());
        }
    }

    @Test
    public void findArticlesByUserIds(){
        Article article = buildTestArticle("1", "Saved", false);

        Article article2 = buildTestArticle("2", "Saved", false);

        repository.saveArticle(article);
        repository.saveArticle(article2);

        Collection<Article> articles = repository.findArticlesByUserId("1");

        for(Article a : articles){
            assertEquals("1", a.getUserId());
        }
    }

    @Test
    public void findArticlesByTitle(){
        Article article = buildTestArticle("1", "Expected", false);

        Article article2 = buildTestArticle("2", "Other", false);

        repository.saveArticle(article);
        repository.saveArticle(article2);

        Collection<Article> articles = repository.findArticlesByTitle("Expected");

        for(Article a : articles){
            assertTrue(a.getTitle().contains("Expected"));
        }
    }

    @Test
    public void deleteArticle(){
        Article article = buildTestArticle("1", "Deleted", false);

        repository.saveArticle(article);

        repository.delete("1");

        Article deletedArticle = repository.findDeletedArticleById("1");
        assertEquals("Deleted Title", deletedArticle.getTitle());
        assertEquals("Deleted Content", deletedArticle.getContent());
        assertEquals("Deleted Tag", deletedArticle.getTag());
        assertEquals("Deleted Category", deletedArticle.getCategory());
        assertTrue(deletedArticle.getDeleted());
    }

    @Test
    public void recoverArticle(){
        Article article = buildTestArticle("1", "Recovered", false);

        repository.saveArticle(article);

        repository.delete("1");
        Article deletedArticle = repository.findDeletedArticleById("1");
        assertEquals(true, deletedArticle.getDeleted());

        repository.recover("1");
        Article recoveredArticle = repository.findArticleById("1");
        assertEquals("Recovered Title", deletedArticle.getTitle());
        assertEquals("Recovered Content", deletedArticle.getContent());
        assertEquals("Recovered Tag", deletedArticle.getTag());
        assertEquals("Recovered Category", deletedArticle.getCategory());
        assertFalse(recoveredArticle.getDeleted());
    }

    private Article buildTestArticle(String id, String action, boolean deleted) {
        Instant fixedTime = Instant.parse("2024-01-01T00:00:00Z");
        Article article = new Article();
        article.setUserId(id);
        article.setArticleId(id);
        article.setTitle(action + " Title");
        article.setContent(action + " Content");
        article.setTag(action + " Tag");
        article.setCategory(action + " Category");
        article.setDate(fixedTime);
        article.setDeleted(deleted);
        return article;
    }

    @Test
    public void saveBookmark() {
        String userId = "u1";
        Bookmark bookmark = new Bookmark(userId);

        repository.saveBookmark(bookmark);

        Bookmark bookmark1 = repository.findBookmarkByUserId(userId);

        assertEquals(userId, bookmark1.getUserId());
    }
}
