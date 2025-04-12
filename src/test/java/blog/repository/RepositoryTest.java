package blog.repository;

import blog.article.Article;
import blog.article.ArticleCollection;
import blog.article.Repository;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RepositoryTest {

    private final Repository repository = new Repository();

    @Test
    public void saveArticle(){
        Article article = new Article();
        article.setUserId("1");
        article.setArticleId("1");
        article.setTitle("Saved Title");
        article.setContent("Saved Content");
        article.setTag("Saved Tag");
        article.setCategory("Saved Category");

        repository.saveArticle(article);

        Article saveedArticle = repository.findArticleById("1");

        assertEquals("Saved Title", saveedArticle.getTitle());
        assertEquals("Saved Content", saveedArticle.getContent());
        assertEquals("Saved Tag", saveedArticle.getTag());
        assertEquals("Saved Category", saveedArticle.getCategory());
    }

    @Test
    public void findArticlesByUserIds(){
        Article article = new Article();
        article.setUserId("1");
        article.setArticleId("1");
        article.setTitle("Saved Title");
        article.setContent("Saved Content");
        article.setTag("Saved Tag");
        article.setCategory("Saved Category");

        Article article2 = new Article();
        article2.setUserId("1");
        article2.setArticleId("2");
        article2.setTitle("Saved Title");
        article2.setContent("Saved Content");
        article2.setTag("Saved Tag");
        article2.setCategory("Saved Category");

        repository.saveArticle(article);
        repository.saveArticle(article2);

        Collection<Article> articles = repository.findArticlesByUserId("1");

        for(Article a : articles){
            assertEquals("1", a.getUserId());
        }
    }

    @Test
    public void findArticlesByTitle(){
        Article article = new Article();
        article.setUserId("1");
        article.setArticleId("1");
        article.setTitle("Expected Title");
        article.setContent("Expected Content");
        article.setTag("Expected Tag");
        article.setCategory("Expected Category");

        Article article2 = new Article();
        article2.setUserId("1");
        article2.setArticleId("2");
        article2.setTitle("Other Title");
        article2.setContent("Other Content");
        article2.setTag("Other Tag");
        article2.setCategory("Other Category");

        repository.saveArticle(article);
        repository.saveArticle(article2);

        Collection<Article> articles = repository.findArticlesByTitle("Expected");

        for(Article a : articles){
            assertTrue(a.getTitle().contains("Expected"));
        }
    }

    @Test
    public void deleteArticle(){
        Article article = new Article();
        article.setUserId("1");
        article.setArticleId("1");
        article.setTitle("Deleted Title");
        article.setContent("Deleted Content");
        article.setTag("Deleted Tag");
        article.setCategory("Deleted Category");

        repository.saveArticle(article);

        repository.delete("1");

        Article deletedArticle = repository.findDeletedArticleById("1");
        assertEquals("Deleted Title", deletedArticle.getTitle());
        assertEquals("Deleted Content", deletedArticle.getContent());
        assertEquals("Deleted Tag", deletedArticle.getTag());
        assertEquals("Deleted Category", deletedArticle.getCategory());
        assertEquals(true, deletedArticle.getDeleted());
    }

    @Test
    public void recoverArticle(){
        Article article = new Article();
        article.setUserId("1");
        article.setArticleId("1");
        article.setTitle("Recovered Title");
        article.setContent("Recovered Content");
        article.setTag("Recovered Tag");
        article.setCategory("Recovered Category");

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
        assertEquals(false, recoveredArticle.getDeleted());
    }

    @Test
    public void saveCollection() {
        String collectionId = "c1";
        String userId = "u1";
        ArticleCollection articleCollection = new ArticleCollection(collectionId, userId);

        repository.saveCollection(articleCollection);

        ArticleCollection articleCollection1 = repository.findCollectionByUserId(userId);

        assertEquals(collectionId, articleCollection1.getCollectionId());
    }
}
